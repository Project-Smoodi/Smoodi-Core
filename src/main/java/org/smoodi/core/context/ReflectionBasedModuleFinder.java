package org.smoodi.core.context;

import org.reflections.Reflections;
import org.smoodi.core.SmoodiFramework;

import java.util.ArrayList;
import java.util.List;

public abstract class ReflectionBasedModuleFinder implements ModuleFinder {

    private static final Reflections reflections =
            new Reflections(SmoodiFramework.getMainClass().getPackageName());

    protected List<Class<?>> collectWithSubTypes(Class<?> klass) {

        final List<Class<?>> subTypes = new ArrayList<>(reflections.getSubTypesOf(klass));
        subTypes.add(klass);

        return subTypes.stream().toList();
    }
}
