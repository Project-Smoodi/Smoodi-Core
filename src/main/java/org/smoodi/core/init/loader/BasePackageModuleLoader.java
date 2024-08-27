package org.smoodi.core.init.loader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasePackageModuleLoader implements ModuleLoader {

    // Module Scanner
    private final static ModuleScanner ms = ModuleScanner.getDefaultInstance();

    @Override
    public void loadModules(String basePackage) {

        final Set<String> moduleNames = ms.getModuleClassNames(basePackage);
    }

    // Singleton
    @Getter
    private final static ModuleLoader instance = new BasePackageModuleLoader();
}
