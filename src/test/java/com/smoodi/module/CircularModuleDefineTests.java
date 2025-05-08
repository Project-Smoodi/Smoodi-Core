package com.smoodi.module;

import com.smoodi.module.general.circular.CircularModuleDefine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.smoodi.core.TestBase;
import org.smoodi.core.module.loader.DefaultModuleClassScanner;
import org.smoodi.core.module.loader.DefaultModuleInitializer;
import org.smoodi.core.module.loader.MainClassPackageBasedModuleLoader;
import org.smoodi.core.module.loader.ModuleLoader;

@SuppressWarnings("NonAsciiCharacters")
@Tag("fork")
public class CircularModuleDefineTests {

    private ModuleLoader createModuleLoader() {
        return new MainClassPackageBasedModuleLoader(
                new DefaultModuleClassScanner(),
                new DefaultModuleInitializer()
        );
    }

    @Test
    public void 순환_참조_모듈_정의_상황() {
        TestBase.initWith(CircularModuleDefine.class);
        final ModuleLoader moduleLoader = createModuleLoader();

        Assertions.assertThrows(IllegalArgumentException.class, moduleLoader::loadModules);
    }
}
