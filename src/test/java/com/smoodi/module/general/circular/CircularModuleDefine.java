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

    public interface InterfaceA {
    }

    public interface InterfaceB {
    }

    public interface InterfaceC {
    }

    @Module
    public static class InterfaceAImpl implements InterfaceA {
        public InterfaceAImpl(
                InterfaceB interfaceB
        ) {
        }
    }

    @Module
    public static class InterfaceBImpl implements InterfaceB {
        public InterfaceBImpl(
                InterfaceC interfaceC
        ) {
        }
    }

    @Module
    public static class InterfaceCImpl implements InterfaceC {
        public InterfaceCImpl(
                InterfaceA interfaceA
        ) {
        }
    }

    public interface InterfaceD {
    }

    @Module
    public static class InterfaceDImpl implements InterfaceD {
        public InterfaceDImpl(
                ModuleD moduleD
        ) {
        }
    }

    @Module
    public static class ModuleD {
        public ModuleD(
                InterfaceD interfaceD
        ) {
        }
    }
}
