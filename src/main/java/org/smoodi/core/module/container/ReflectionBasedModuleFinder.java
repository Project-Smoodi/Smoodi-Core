package org.smoodi.core.module.container;

import org.reflections.Reflections;
import org.smoodi.core.SmoodiFramework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ReflectionBasedModuleFinder implements ModuleFinder {

    private final List<Reflections> reflections = List.of(
            new Reflections(SmoodiFramework.getMainClass().getPackage().getName()),
            new Reflections(SmoodiFramework.SMOODI_PACKAGE_PREFIX)
    );

    protected List<Class<?>> collectWithSubTypes(Class<?> klass) {

        final Set<Class<?>> subTypes = new HashSet<>();

        for (Reflections reflection : reflections) {
            subTypes.addAll(reflection.getSubTypesOf(klass));
        }

        subTypes.add(klass);

        // Set to unmodifiable
        return subTypes.stream().toList();
    }
}
