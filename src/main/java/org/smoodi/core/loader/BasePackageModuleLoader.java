package org.smoodi.core.loader;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BasePackageModuleLoader implements PackageBasedModuleLoader {

    // Module Scanner
    private final ModuleScanner ms = new DefaultModuleScanner();

    // Module Initializer
    private final ModuleInitializer mi = new DefaultModuleInitializer();

    @Override
    public int loadModules(String basePackage) {

        final List<String> moduleNames = ms.getModuleClassNames(basePackage).stream().toList();

        mi.initialize(moduleNames);

        return moduleNames.size();
    }
}
