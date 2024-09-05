package org.smoodi.core.module.loader.initializer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class DefaultModuleInitializer implements ModuleInitializer {

    private final ModuleInitConstructorSearcher searcher =
            new DefaultModuleInitConstructorSearcher();

    private final ModuleInitConstructorRunner mr = new ModuleInitConstructorRunner();

    @Override
    public void initialize(List<String> moduleNames) {

        final List<Constructor<?>> constructors = new ArrayList<>();

        for (String moduleName : moduleNames) {
            try {
                final Constructor<?> constructor =
                        searcher.findModuleInitConstructor(
                                Class.forName(moduleName)
                        );
                constructors.add(constructor);
            } catch (ClassNotFoundException ignored) {
            }
        }

        mr.runConstructor(constructors);
    }
}
