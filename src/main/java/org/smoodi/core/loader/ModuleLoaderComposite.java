package org.smoodi.core.loader;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;

import java.time.LocalDateTime;

@Slf4j
public class ModuleLoaderComposite implements PackageBasedModuleLoader {

    private final PackageBasedModuleLoader packageBasedModuleLoader =
            new BasePackageModuleLoader();

    private final StaticModuleLoader smoodiProjectModuleLoader =
            new SmoodiProjectModuleLoader((BasePackageModuleLoader) packageBasedModuleLoader);

    private final StaticModuleLoader staticModuleLoader =
            new DefaultStaticModuleLoader();

    @Override
    public void loadModules(String basePackage) {
        log.info(LOG_PREFIX + "Module loading started at {}", LocalDateTime.now());

        staticModuleLoader.loadModules();
        smoodiProjectModuleLoader.loadModules();
        packageBasedModuleLoader.loadModules(SmoodiFramework.getMainClass().getPackage().getName());

        log.info(LOG_PREFIX + "Total \"{}\" modules are loaded at {}", SmoodiFramework.getModuleContainer().getModuleCount(), LocalDateTime.now());
    }
}
