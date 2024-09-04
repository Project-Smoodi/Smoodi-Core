package org.smoodi.core.module.loader;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.module.loader.initializer.DefaultModuleInitializer;
import org.smoodi.core.module.loader.initializer.ModuleInitializer;

import java.time.LocalDateTime;

@Slf4j
public class ModuleLoaderComposite implements ModuleLoader {

    private final ModuleNameScanner moduleNameScanner = new DefaultModuleNameScanner();

    private final ModuleInitializer moduleInitializer = new DefaultModuleInitializer();

    private final PackageBasedModuleLoader packageBasedModuleLoader =
            new BasePackageModuleLoader();

    private final ModuleLoader smoodiProjectModuleLoader =
            new SmoodiProjectModuleLoader(moduleNameScanner, moduleInitializer);

    private final ModuleLoader staticModuleLoader =
            new DefaultStaticModuleLoader();

    @Override
    public int loadModules() {
        log.info(LOG_PREFIX + "Module loading started at {}", LocalDateTime.now());

        int totalLoadedModules = 0;

        totalLoadedModules += staticModuleLoader.loadModules();
        totalLoadedModules += smoodiProjectModuleLoader.loadModules();
        totalLoadedModules += packageBasedModuleLoader.loadModules(SmoodiFramework.getMainClass().getPackage().getName());

        final int moduleContainerModules = SmoodiFramework.getModuleContainer().getModuleCount();

        if (log.isDebugEnabled() && totalLoadedModules != moduleContainerModules) {
            log.debug("Smoodi driven loaders loaded {} modules BUT ModuleContainer containing {} modules.", totalLoadedModules, moduleContainerModules);
        }

        log.info(LOG_PREFIX + "Total \"{}\" modules are loaded at {}", totalLoadedModules, LocalDateTime.now());

        return totalLoadedModules;
    }
}
