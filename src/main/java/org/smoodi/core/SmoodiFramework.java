package org.smoodi.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.annotation.NotNull;
import org.smoodi.core.annotation.StaticModule;
import org.smoodi.core.module.container.DefaultModuleContainer;
import org.smoodi.core.module.container.ModuleContainer;
import org.smoodi.core.util.PackageVerify;

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

    private SmoodiBootStrap starter = null;

    SmoodiBootStrap getStarter() {
        if (starter == null) {
            starter = new SmoodiBootStrap();
            log.info("{} was initialized", SmoodiBootStrap.class.getName());
        }
        return starter;
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

    synchronized static void initSmoodiFramework(@NotNull Class<?> mainClass) {
        assert mainClass != null;

        if (instance != null) {
            log.warn("Smoodi framework already initialized BUT call init method again.");
            return;
        }

        SmoodiState.setState(SmoodiState.INITIALIZING);
        PackageVerify.verify(mainClass.getPackageName());
        getInstance();
        SmoodiFramework.mainClass = mainClass;
    }

    synchronized static void finishBootStrap() {
        if (SmoodiState.getState().equals(SmoodiState.RUNNING)) {
            log.warn("Smoodi framework already initialized BUT call init method again.");
            return;
        }

        getInstance().starter = null;
        SmoodiState.setState(SmoodiState.RUNNING);
    }

    synchronized static void kill() {
        if (SmoodiState.getState().equals(SmoodiState.STOPPED)) {
            log.warn("Smoodi framework already stopped BUT call kill method again.");
            return;
        }

        SmoodiInterrupter.interrupt();
    }
}