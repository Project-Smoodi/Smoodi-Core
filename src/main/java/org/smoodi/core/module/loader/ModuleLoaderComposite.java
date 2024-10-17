package org.smoodi.core.module.loader;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.module.loader.initializer.DefaultModuleInitializer;
import org.smoodi.core.module.loader.initializer.ModuleInitializer;

import java.time.LocalDateTime;

@Slf4j
public class ModuleLoaderComposite implements ModuleLoader {

    private final ModuleClassScanner moduleClassScanner = new DefaultModuleClassScanner();

    private final ModuleInitializer moduleInitializer = new DefaultModuleInitializer();

    private final ModuleLoader packageBasedModuleLoader =
            new MainClassPackageBasedModuleLoader(moduleClassScanner, moduleInitializer);

    private final ModuleLoader smoodiProjectModuleLoader =
            new SmoodiProjectModuleLoader(moduleClassScanner, moduleInitializer);

    private final ModuleLoader staticModuleLoader =
            new DefaultStaticModuleLoader();

    @Override
    public int loadModules() {

        log.info(LOG_PREFIX + "Module loading started at {}", LocalDateTime.now());

        int totalLoadedModules = 0;

        totalLoadedModules += staticModuleLoader.loadModules();
        totalLoadedModules += smoodiProjectModuleLoader.loadModules();
        totalLoadedModules += packageBasedModuleLoader.loadModules();

        final int moduleContainerModules = SmoodiFramework.getInstance().getModuleContainer().getModuleCount();

        if (log.isDebugEnabled() && totalLoadedModules != moduleContainerModules) {
            log.warn("Smoodi driven loaders loaded {} modules BUT ModuleContainer containing {} modules.", totalLoadedModules, moduleContainerModules);
        }

        log.info(LOG_PREFIX + "Total \"{}\" modules are loaded at {}", totalLoadedModules, LocalDateTime.now());

        return totalLoadedModules;
    }
}
