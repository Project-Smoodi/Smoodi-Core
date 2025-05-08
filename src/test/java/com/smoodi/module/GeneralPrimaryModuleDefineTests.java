package com.smoodi.module;

import com.smoodi.module.generalprimarymodule.GeneralPrimaryModuleDefine;
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
public class GeneralPrimaryModuleDefineTests {

    private ModuleLoader createModuleLoader() {
        return new MainClassPackageBasedModuleLoader(
                new DefaultModuleClassScanner(),
                new DefaultModuleInitializer()
        );
    }

    @Test
    public void 정상적인_기본_모듈_정의_상황() {
        TestBase.initWith(GeneralPrimaryModuleDefine.class);
        final ModuleLoader moduleLoader = createModuleLoader();

        Assertions.assertDoesNotThrow(moduleLoader::loadModules);
    }
}
