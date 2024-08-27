package org.smoodi.core.loader;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class DefaultModuleInitializer implements ModuleInitializer {

    private final ModuleConstructorSearcher searcher = new ConfigurableModuleConstructorSearcherProvider();

    private final ModuleConstructorRunner mr = new ModuleConstructorRunner();

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
            final Constructor<?> constructor = searcher.findConstructor(klass);

            constructors.add(constructor);
        });

        mr.create(constructors);
    }
}
