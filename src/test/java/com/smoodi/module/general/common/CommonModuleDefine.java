package com.smoodi.module.general.common;

import org.smoodi.core.annotation.Module;

public class CommonModuleDefine {

    @Module
    public static class ModuleA {
        public ModuleA(
                ModuleB moduleB,
                ModuleC moduleC
        ) {
        }
    }

    @Module
    public static class ModuleB {
        public ModuleB(
                ModuleC moduleC
        ) {
        }
    }

    @Module
    public static class ModuleC {
    }
}
