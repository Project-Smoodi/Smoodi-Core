package org.smoodi.core.module.loader;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;

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
        var userDefinedModules = packageBasedModuleLoader.loadModules();
        totalLoadedModules += userDefinedModules;

        final int moduleContainerModules = SmoodiFramework.getInstance().getModuleContainer().getModuleCount();

        if (userDefinedModules == 0) {
            log.warn("The number of modules other than smoodi driven module is \"0\". The classes in your project may be \"Module-Private\". If so, smoodi cannot scan your modules.");
        }

        if (log.isDebugEnabled() && totalLoadedModules != moduleContainerModules) {
            log.warn("Smoodi driven loaders loaded {} modules BUT ModuleContainer containing {} modules.", totalLoadedModules, moduleContainerModules);
        }

        log.info(LOG_PREFIX + "Total \"{}\" modules are loaded at {}", totalLoadedModules, LocalDateTime.now());

        return totalLoadedModules;
    }
}
