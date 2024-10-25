package org.smoodi.core.module.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.module.ModuleType;

import java.util.Set;

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
            log.debug(LOG_PREFIX + "{} load \"0\" modules. Because main class package name is not supported (duplicated with Smoodi package name.) : {}", this.getClass().getName(), SmoodiFramework.getMainClass().getPackage().getName());
            return 0;
        }

        final Set<ModuleType<?>> moduleClasses = ms.getModuleClasses(
                SmoodiFramework.getMainClass().getPackage().getName()
        );

        mi.initialize(moduleClasses);

        return moduleClasses.size();
    }
}
