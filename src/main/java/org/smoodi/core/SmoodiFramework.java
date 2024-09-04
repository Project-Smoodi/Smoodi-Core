package org.smoodi.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.module.container.DefaultModuleContainer;
import org.smoodi.core.module.container.ModuleContainer;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SmoodiFramework {

    private ModuleContainer moduleContainer = null;

    public ModuleContainer getModuleContainer() {
        if (moduleContainer == null) {
            moduleContainer = new DefaultModuleContainer();
            log.info("{} was initialized", ModuleContainer.class.getName());
        }
        return moduleContainer;
    }

    private SmoodiBootStrap starter = null;

    public SmoodiBootStrap getStarter() {
        if (starter == null) {
            starter = new SmoodiBootStrap();
            log.info("{} was initialized", SmoodiBootStrap.class.getName());
        }
        return SmoodiFramework.getInstance().starter;
    }

    @Getter
    private static Class<?> mainClass = null;

    protected static SmoodiFramework instance = null;

    public static SmoodiFramework getInstance() {
        if (instance == null) {
            instance = new SmoodiFramework();
            log.info("{} was initialized", SmoodiFramework.class.getName());
        }
        return instance;
    }

    public static void initSmoodiFramework(Class<?> mainClass) {
        if (instance != null) {
            return;
        }

        getInstance();

        SmoodiFramework.mainClass = mainClass;

        SubprojectPackageManager.addSubproject(
                "Smoodi-Core",
                SmoodiFramework.class.getPackage()
        );
    }

    public static void finishBootStrap() {
        getInstance().starter = null;
    }
}