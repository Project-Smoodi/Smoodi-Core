package org.smoodi.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.init.LoggerInitializer;
import org.smoodi.core.module.loader.ModuleLoader;
import org.smoodi.core.module.loader.ModuleLoaderComposite;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static org.smoodi.core.SmoodiFramework.finishBootStrap;
import static org.smoodi.core.SmoodiFramework.getInstance;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class SmoodiBootStrap {

    private ModuleLoader moduleLoader;

    private boolean initialized = false;

    private void init() {
        if (initialized) {
            return;
        }

        moduleLoader = new ModuleLoaderComposite();

        initialized = true;
    }

    public synchronized static void startSmoodi(Class<?> mainClass) {
        final LocalDateTime startedAt = LocalDateTime.now();

        try {
            LoggerInitializer.configureLogback();
            SmoodiFramework.initSmoodiFramework(mainClass);
            SmoodiFramework.getInstance().getStarter().init();

            getInstance().getStarter().moduleLoader.loadModules();

            new SubprojectBootStrapRunner().run();

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

        finishBootStrap();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            SmoodiFramework.kill();
        }
    }

    private static final class SubprojectBootStrapRunner {

        private final Set<SubprojectBootStrap> bootStraps =
                SmoodiFramework.getInstance().getModuleContainer().getModulesByClass(
                        SubprojectBootStrap.class
                );

        public void run() {
            log.info("Smoodi subprojects bootstrap started.");
            bootStraps.forEach(SubprojectBootStrap::start);
        }
    }
}
