package org.smoodi.core.module.loader;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.module.loader.initializer.DefaultModuleInitializer;
import org.smoodi.core.module.loader.initializer.ModuleInitializer;

import java.util.List;

@Slf4j
public class BasePackageModuleLoader implements PackageBasedModuleLoader {

    // Module name Scanner
    private final ModuleNameScanner ms = new DefaultModuleNameScanner();

    // Module Initializer
    private final ModuleInitializer mi = new DefaultModuleInitializer();

    @Override
    public int loadModules(String basePackage) {

        final List<String> moduleNames = ms.getModuleClassNames(basePackage).stream().toList();

        mi.initialize(moduleNames);

        return moduleNames.size();
    }
}
