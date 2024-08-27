package org.smoodi.core.loader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasePackageModuleLoader implements ModuleLoader {

    // Module Scanner
    private static final ModuleScanner ms = ModuleScanner.getDefaultInstance();

    private static final ModuleInitializer mi = ModuleInitializer.getInstance();

    @Override
    public void loadModules(String basePackage) {

        final List<String> moduleNames = ms.getModuleClassNames(basePackage).stream().toList();

        mi.initialize(moduleNames);
    }

    // Singleton
    @Getter
    private final static ModuleLoader instance = new BasePackageModuleLoader();
}
