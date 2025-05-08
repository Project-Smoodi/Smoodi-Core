package com.smoodi.module;

import com.smoodi.module.general.common.CommonModuleDefine;
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
public class CommonModuleDefineTests {

    private ModuleLoader createModuleLoader() {
        return new MainClassPackageBasedModuleLoader(
                new DefaultModuleClassScanner(),
                new DefaultModuleInitializer()
        );
    }

    @Test
    public void 일반적인_모듈_정의_상황() {
        TestBase.initWith(CommonModuleDefine.class);
        final ModuleLoader moduleLoader = createModuleLoader();

        Assertions.assertDoesNotThrow(moduleLoader::loadModules);
    }
}
