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
    private final ModuleNameScanner ms;

    // Module Initializer
    private final ModuleInitializer mi;

    @Override
    public int loadModules() {

        final List<String> moduleNames = ms.getModuleClassNames(
                SmoodiFramework.getMainClass().getPackage().getName()
        ).stream().toList();

        mi.initialize(moduleNames);

        return moduleNames.size();
    }
}
