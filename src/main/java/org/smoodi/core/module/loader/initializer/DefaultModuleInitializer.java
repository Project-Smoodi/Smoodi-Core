package org.smoodi.core.module.loader.initializer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class DefaultModuleInitializer implements ModuleInitializer {

    private final ModuleInitConstructorSearcher searcher = new ConfigurableModuleInitConstructorSearcherProvider();

    private final ModuleInitConstructorRunner mr = new ModuleInitConstructorRunner();

    @Override
    public void initialize(List<String> moduleNames) {

        final List<Constructor<?>> constructors = new ArrayList<>();

        moduleNames.forEach((moduleName) -> {
            Class<?> klass;
            try {
                klass = Class.forName(moduleName);
            } catch (ClassNotFoundException e) {
                return;
            }
            final Constructor<?> constructor = searcher.findModuleInitConstructor(klass);

            constructors.add(constructor);
        });

        mr.runConstructor(constructors);
    }
}
