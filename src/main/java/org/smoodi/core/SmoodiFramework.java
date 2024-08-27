package org.smoodi.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.container.DefaultModuleContainer;
import org.smoodi.core.container.ModuleContainer;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmoodiFramework {

    private ModuleContainer moduleContainer = null;

    public static ModuleContainer getModuleContainer() {
        if (getInstance().moduleContainer == null) {
            getInstance().moduleContainer = new DefaultModuleContainer();
            log.info("{} was initialized", ModuleContainer.class.getName());
        }
        return SmoodiFramework.getInstance().moduleContainer;
    }

    private SmoodiBootStrap starter = null;

    public static SmoodiBootStrap getStarter() {
        if (getInstance().starter == null) {
            getInstance().starter = new SmoodiBootStrap();
            log.info("{} was initialized", SmoodiBootStrap.class.getName());
        }
        return SmoodiFramework.getInstance().starter;
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

    public static void startBootStrap(Class<?> mainClass) {
        if (instance != null) {
            return;
        }
        SmoodiFramework.mainClass = mainClass;
        getInstance();
    }

    public static void finishBootStrap() {
        getInstance().starter = null;
    }
}