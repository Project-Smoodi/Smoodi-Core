package org.smoodi.core.loader;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BasePackageModuleLoader implements ModuleLoader {

    // Module Scanner
    private static final ModuleScanner ms = new DefaultModuleScanner();

    // Module Initializer
    private static final ModuleInitializer mi = new DefaultModuleInitializer();

    @Override
    public void loadModules(String basePackage) {

        final List<String> moduleNames = ms.getModuleClassNames(basePackage).stream().toList();

        mi.initialize(moduleNames);
    }
}
