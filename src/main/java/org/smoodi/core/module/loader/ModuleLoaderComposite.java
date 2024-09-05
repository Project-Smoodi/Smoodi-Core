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

    private ModuleLoader packageBasedModuleLoader;

    private ModuleLoader smoodiProjectModuleLoader;

    private ModuleLoader staticModuleLoader;

    private boolean initialized = false;

    private void init() {
        if (initialized) {
            return;
        }

        packageBasedModuleLoader =
                new MainClassPackageBasedModuleLoader(moduleNameScanner, moduleInitializer);
        smoodiProjectModuleLoader =
                new SmoodiProjectModuleLoader(moduleNameScanner, moduleInitializer);
        staticModuleLoader =
                new DefaultStaticModuleLoader();

        this.initialized = true;
    }

    @Override
    public int loadModules() {
        init();

        log.info(LOG_PREFIX + "Module loading started at {}", LocalDateTime.now());

        int totalLoadedModules = 0;

        totalLoadedModules += staticModuleLoader.loadModules();
        totalLoadedModules += smoodiProjectModuleLoader.loadModules();
        totalLoadedModules += packageBasedModuleLoader.loadModules();

        final int moduleContainerModules = SmoodiFramework.getInstance().getModuleContainer().getModuleCount();

        if (log.isDebugEnabled() && totalLoadedModules != moduleContainerModules) {
            log.debug("Smoodi driven loaders loaded {} modules BUT ModuleContainer containing {} modules.", totalLoadedModules, moduleContainerModules);
        }

        log.info(LOG_PREFIX + "Total \"{}\" modules are loaded at {}", totalLoadedModules, LocalDateTime.now());

        return totalLoadedModules;
    }
}
