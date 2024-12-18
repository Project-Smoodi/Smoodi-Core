package com.smoodi.module.general.circular;

import org.smoodi.core.annotation.Module;

public class CircularModuleDefine {

    @Module
    public static class ModuleA {
        public ModuleA(
                ModuleB moduleB
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
        public ModuleC(
                ModuleA moduleA
        ) {
        }
    }
}
