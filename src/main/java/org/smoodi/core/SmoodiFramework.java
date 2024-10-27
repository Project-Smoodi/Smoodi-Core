package org.smoodi.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.annotation.NotNull;
import org.smoodi.core.annotation.StaticModule;
import org.smoodi.core.init.LoggerInitializer;
import org.smoodi.core.module.container.DefaultModuleContainer;
import org.smoodi.core.module.container.ModuleContainer;
import org.smoodi.core.module.loader.ModuleLoaderComposite;
import org.smoodi.core.util.LazyInitUnmodifiableCollection;
import org.smoodi.core.util.PackageVerify;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@StaticModule
public final class SmoodiFramework {

    public static final String SMOODI_PACKAGE_PREFIX = "org.smoodi";

    private ModuleContainer moduleContainer = null;

    public ModuleContainer getModuleContainer() {
        if (moduleContainer == null) {
            moduleContainer = new DefaultModuleContainer();
            log.info("{} was initialized", ModuleContainer.class.getName());
        }
        return moduleContainer;
    }

    @Getter
    private static Class<?> mainClass = null;

    private static SmoodiFramework instance = null;

    public static SmoodiFramework getInstance() {
        if (instance == null) {
            instance = new SmoodiFramework();
            log.info("{} was initialized", SmoodiFramework.class.getName());
        }
        return instance;
    }

    synchronized static void startSmoodi(@NotNull final Class<?> mainClass) {
        assert mainClass != null;

        if (!SmoodiState.getState().equals(SmoodiState.SLEEPING)) {
            log.error("Smoodi framework already started (or initializing)");
            return;
        }

        SmoodiState.setState(SmoodiState.INITIALIZING);

        PackageVerify.verify(mainClass.getPackageName());
        getInstance();
        SmoodiFramework.mainClass = mainClass;

        SmoodiBootStrap.startBootStrap();

        SmoodiState.setState(SmoodiState.RUNNING);

        SubprojectPostBootStrapRunner.run();
    }

    synchronized static void kill() {
        if (SmoodiState.getState().equals(SmoodiState.STOPPED)) {
            log.warn("Smoodi framework already stopped BUT call kill method again.");
            return;
        }

        SmoodiInterrupter.interrupt();
    }

    private static class SmoodiBootStrap {

        private synchronized static void startBootStrap() {
            final LocalDateTime startedAt = LocalDateTime.now();

            LoggerInitializer.configureLogback();

            try {
                loadModules();

                runSubprojectBootStraps();

            } catch (Throwable error) {
                log.error(error.getMessage(), error);
                SmoodiState.setState(SmoodiState.ERRORED);
                return;
            }

            final LocalDateTime finishedAt = LocalDateTime.now();
            log.info(
                    "Smoodi started on {} seconds. Started at : {}, Initialize finished at : {}",
                    (Duration.between(startedAt, finishedAt).getNano() / 1_000_000_000.0),
                    startedAt, finishedAt
            );

            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                SmoodiFramework.kill();
            }
        }

        private static void loadModules() {
            new ModuleLoaderComposite().loadModules();
        }

        private static void runSubprojectBootStraps() {
            log.debug("Smoodi subprojects bootstrap started.");
            SmoodiFramework.getInstance().getModuleContainer().getModulesByClass(
                    SubprojectBootStrap.class
            ).forEach(SubprojectBootStrap::start);
        }
    }

    private static class SubprojectPostBootStrapRunner {

        private synchronized static void run() {
            SmoodiFramework.getInstance().getModuleContainer()
                    .getModulesByClass(SubprojectPostBootStrap.class)
                    .forEach(SubprojectPostBootStrap::post);
        }
    }

    @Slf4j
    private static class SmoodiInterrupter {

        private static final LazyInitUnmodifiableCollection<Set<SubprojectInterrupt>> subprojects =
                new LazyInitUnmodifiableCollection<>();

        synchronized static void interrupt() {
            SmoodiState.setState(SmoodiState.STOPPING);
            log.info("Stopping smoodi framework...");

            subprojects.initWith(
                    SmoodiFramework.getInstance().getModuleContainer()
                            .getModulesByClass(SubprojectInterrupt.class)
            ).get().forEach(SubprojectInterrupt::interrupt);
        }
    }
}