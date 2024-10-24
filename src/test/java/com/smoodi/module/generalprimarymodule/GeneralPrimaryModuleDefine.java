package com.smoodi.module.generalprimarymodule;

import org.smoodi.core.annotation.Module;

public class GeneralPrimaryModuleDefine {

    public interface GeneralModuleInterface {
    }

    @Module
    public static class ImplGeneralModuleA implements GeneralModuleInterface {
    }

    @Module
    public static class ImplGeneralModuleB implements GeneralModuleInterface {
    }

    @Module(isPrimary = true)
    public static class ImplGeneralModuleC implements GeneralModuleInterface {
    }

    @Module
    public static class DependencyGeneralModule {
        public DependencyGeneralModule(
                GeneralModuleInterface generalModuleInterface
        ) {
        }
    }
}
