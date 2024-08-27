package org.smoodi.core.context;

import org.reflections.Reflections;
import org.smoodi.core.SmoodiStarter;

import java.util.List;

public abstract class ReflectionBasedModuleFinder implements ModuleFinder {

    private static final Reflections reflections = new Reflections(SmoodiStarter.mainClass.getPackageName());

    protected List<Class<?>> collectWithSubTypes(Class<?> klass) {

        final var subTypes = (List<Class<?>>) reflections.getSubTypesOf(klass);
        subTypes.add(klass);

        return subTypes.stream().toList();
    }
}
