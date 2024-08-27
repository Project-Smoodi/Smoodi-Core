package org.smoodi.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.smoodi.core.container.DefaultModuleContainer;
import org.smoodi.core.container.ModuleContainer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmoodiFramework {

    private final ModuleContainer moduleContainer = new DefaultModuleContainer();

    public static ModuleContainer getModuleContainer() {
        return SmoodiFramework.getInstance().moduleContainer;
    }

    private SmoodiBootStrap starter = new SmoodiBootStrap();

    public static SmoodiBootStrap getStarter() {
        return SmoodiFramework.getInstance().starter;
    }

    @Getter
    private static Class<?> mainClass = null;

    private static SmoodiFramework instance = null;

    public static SmoodiFramework getInstance() {
        if (instance == null) {
            instance = new SmoodiFramework();
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