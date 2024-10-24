package com.smoodi.module.duplicateprimarymodule;

import org.smoodi.core.annotation.Module;

public class DuplicatePrimaryModuleDefine {

    public interface DuplicateModuleInterface {
    }

    @Module
    public static class ImplDuplicateModuleA implements DuplicateModuleInterface {
    }

    @Module
    public static class ImplDuplicateModuleB implements DuplicateModuleInterface {
    }

    @Module(isPrimary = true)
    public static class ImplDuplicateModuleC implements DuplicateModuleInterface {
    }

    @Module(isPrimary = true)
    public static class ImplDuplicateModuleD implements DuplicateModuleInterface {
    }

    @Module
    public static class DependencyDuplicateModule {
        public DependencyDuplicateModule(
                DuplicateModuleInterface duplicateModuleInterface
        ) {
        }
    }
}
