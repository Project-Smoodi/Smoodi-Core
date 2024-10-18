package org.smoodi.core.module.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.module.ModuleType;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class SmoodiProjectModuleLoader implements ModuleLoader {

    private final ModuleClassScanner moduleClassScanner;

    private final ModuleInitializer moduleInitializer;

    @Override
    public int loadModules() {

        final Set<ModuleType<?>> moduleClasses =
                moduleClassScanner.getModuleClasses(SmoodiFramework.SMOODI_PACKAGE_PREFIX);

        moduleInitializer.initialize(moduleClasses);

        log.info(LOG_PREFIX + "Smoodi project total {} modules are loaded.", moduleClasses.size());

        return moduleClasses.size();
    }
}
