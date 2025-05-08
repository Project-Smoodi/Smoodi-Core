package com.smoodi.module;

import com.smoodi.module.duplicateprimarymodule.DuplicatePrimaryModuleDefine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.smoodi.core.TestBase;
import org.smoodi.core.module.ModuleDeclareError;
import org.smoodi.core.module.loader.DefaultModuleClassScanner;
import org.smoodi.core.module.loader.DefaultModuleInitializer;
import org.smoodi.core.module.loader.MainClassPackageBasedModuleLoader;
import org.smoodi.core.module.loader.ModuleLoader;

@SuppressWarnings("NonAsciiCharacters")
@Tag("fork")
public class DuplicatePrimaryModuleDefineTests {

    private ModuleLoader createModuleLoader() {
        return new MainClassPackageBasedModuleLoader(
                new DefaultModuleClassScanner(),
                new DefaultModuleInitializer()
        );
    }

    @Test
    public void 기본_모듈_다수_정의_상황() {
        TestBase.initWith(DuplicatePrimaryModuleDefine.class);
        final ModuleLoader moduleLoader = createModuleLoader();

        Assertions.assertThrows(ModuleDeclareError.class, moduleLoader::loadModules);
    }
}
