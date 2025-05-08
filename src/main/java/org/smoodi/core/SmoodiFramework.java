package org.smoodi.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.annotation.NotNull;
import org.smoodi.core.annotation.StaticModule;
import org.smoodi.core.init.LoggerInitializer;
import org.smoodi.core.lifecycle.Lifecycle;
import org.smoodi.core.lifecycle.LifecycleDefineError;
import org.smoodi.core.module.container.DefaultModuleContainer;
import org.smoodi.core.module.container.ModuleContainer;
import org.smoodi.core.util.PackageVerify;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@StaticModule
public final class SmoodiFramework implements Lifecycle {

    public static final String SMOODI_PACKAGE_PREFIX = "org.smoodi";

    @Getter
    @Setter
    private Lifecycle.State state = State.SLEEPING;

    @Getter
    private static Class<?> mainClass = null;

    private ModuleContainer moduleContainer = null;

    public ModuleContainer getModuleContainer() {
        if (moduleContainer == null) {
            moduleContainer = new DefaultModuleContainer();
            log.info("{} was initialized", ModuleContainer.class.getName());
        }
        return moduleContainer;
    }

    private Processor smoodiCoreProcessor = null;

    public Processor getSmoodiCoreProcessor() {
        if (smoodiCoreProcessor == null) {
            smoodiCoreProcessor = new SmoodiCoreProcessor();
            log.info("{} was initialized", SmoodiCoreProcessor.class.getName());
        }
        return smoodiCoreProcessor;
    }

    private static SmoodiFramework instance = null;

    public static SmoodiFramework getInstance() {
        if (instance == null) {
            instance = new SmoodiFramework();
            log.info("{} was initialized", SmoodiFramework.class.getName());
        }
        return instance;
    }

    public synchronized static void startSmoodi(@NotNull final Class<?> mainClass) {
        assert mainClass != null;

        SmoodiFramework.mainClass = mainClass;

        getInstance().startSmoodiLocal();
    }

    private void startSmoodiLocal() {
        if (getState() != State.SLEEPING) {
            throw new LifecycleDefineError();
        }

        initialize();
        start();
    }

    private synchronized void initialize() {
        setState(State.INITIALIZING);

        LoggerInitializer.configureLogback();
        PackageVerify.verify(mainClass.getPackageName());

        setState(State.INITIALIZED);

        log.debug("{} was initialized", SmoodiFramework.class.getName());
    }

    private synchronized void start() {
        final LocalDateTime calledAt = LocalDateTime.now();
        setState(State.STARTING);

        getSmoodiCoreProcessor().start();

        setState(State.RUNNING);
        final LocalDateTime finishedAt = LocalDateTime.now();
        log.info(
                "Smoodi started on {} seconds. Started at : {}, Initialize finished at : {}",
                (Duration.between(calledAt, finishedAt).getNano() / 1_000_000_000.0),
                calledAt, finishedAt
        );
    }

    synchronized static void kill() {
        switch (getInstance().getState()) {
            case RUNNING: {
                getInstance().getSmoodiCoreProcessor().stop();
                break;
            }
            case SLEEPING, INITIALIZING, INITIALIZED, STARTING:
                log.warn("Smoodi framework was not started BUT kill method called.");
                break;
            default:
                log.warn("Smoodi framework already stopped BUT call kill method again.");
        }
    }
}