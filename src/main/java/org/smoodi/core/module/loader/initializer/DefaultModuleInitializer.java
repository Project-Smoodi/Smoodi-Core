package org.smoodi.core.module.loader.initializer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class DefaultModuleInitializer implements ModuleInitializer {

    private final ModuleInitConstructorSearcher searcher =
            new DefaultModuleInitConstructorSearcher();

    private final ModuleInitConstructorRunner mr = new ModuleInitConstructorRunner();

    @Override
    public void initialize(List<Class<?>> moduleClasses) {

        final List<Constructor<?>> constructors = new ArrayList<>();

        for (Class<?> moduleClass : moduleClasses) {
            final Constructor<?> constructor =
                    searcher.findModuleInitConstructor(moduleClass);
            constructors.add(constructor);
        }

        mr.runConstructor(constructors);
    }
}
