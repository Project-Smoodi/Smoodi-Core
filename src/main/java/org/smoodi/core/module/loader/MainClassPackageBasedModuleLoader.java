package org.smoodi.core.module.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.module.loader.initializer.ModuleInitializer;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MainClassPackageBasedModuleLoader implements ModuleLoader {

    // Module name Scanner
    private final ModuleClassScanner ms;

    // Module Initializer
    private final ModuleInitializer mi;

    @Override
    public int loadModules() {

        if (SmoodiFramework.getMainClass().getPackage().getName().startsWith(SmoodiFramework.SMOODI_PACKAGE_PREFIX)) {
            log.debug(LOG_PREFIX + "{} load 0 modules because main class package name is not supported (duplicated with Smoodi package name.) : {}", getClass().getSimpleName(), SmoodiFramework.getMainClass().getPackage().getName());
            return 0;
        }

        final List<Class<?>> moduleClasses = ms.getModuleClasses(
                SmoodiFramework.getMainClass().getPackage().getName()
        ).stream().toList();

        mi.initialize(moduleClasses);

        return moduleClasses.size();
    }
}
