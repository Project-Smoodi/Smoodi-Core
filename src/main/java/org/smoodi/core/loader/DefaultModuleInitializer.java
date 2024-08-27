package org.smoodi.core.loader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultModuleInitializer implements ModuleInitializer {

    private final ModuleConstructorSearcher searcher = ModuleConstructorSearcher.getInstance();

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

        ModuleConstructorRunner.getInstance().create(constructors);
    }

    @Getter
    private static final ModuleInitializer instance = new DefaultModuleInitializer();
}
