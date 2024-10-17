package org.smoodi.core.module.loader.initializer;

import org.smoodi.core.module.ModuleType;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultModuleInitializer implements ModuleInitializer {

    private final ModuleInitConstructorRunner mr = new ModuleInitConstructorRunner();

    @Override
    public void initialize(Set<ModuleType<?>> moduleTypes) {
        mr.runConstructor(moduleTypes);
    }
}
