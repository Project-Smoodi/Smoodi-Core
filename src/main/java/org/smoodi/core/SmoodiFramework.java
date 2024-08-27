package org.smoodi.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.smoodi.core.container.DefaultModuleContainer;
import org.smoodi.core.container.ModuleContainer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SmoodiFramework {

    @Getter
    private static final ModuleContainer moduleContainer = new DefaultModuleContainer();

    @Getter
    private static SmoodiBootStrap starter = new SmoodiBootStrap();

    @Getter
    private static Class<?> mainClass = null;

    private static boolean isBootStrapStarted = false;
    private static boolean isBootStrapFinished = false;

    public static void startBootStrap(Class<?> mainClass) {
        if (isBootStrapStarted) {
            return;
        }

        SmoodiFramework.mainClass = mainClass;

        isBootStrapStarted = true;
    }

    public static void finishBootStrap() {
        if (isBootStrapFinished) {
            return;
        }

        SmoodiFramework.starter = null;

        isBootStrapFinished = true;
    }
}