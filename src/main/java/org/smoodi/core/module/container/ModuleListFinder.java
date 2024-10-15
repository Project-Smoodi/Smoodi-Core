package org.smoodi.core.module.container;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModuleListFinder extends ReflectionBasedModuleFinder {

    @Override
    public <T> Set<T> find(Map<Class<?>, Set<Object>> objects, Class<T> klass) {
        final List<Class<?>> subTypes = collectWithSubTypes(klass);

        final Set<Object> found = new HashSet<>();

        subTypes.forEach(subType -> {
            if (objects.get(subType) != null) {
                found.addAll(
                        objects.get(subType)
                );
            }
        });

        //noinspection unchecked
        return (Set<T>) found;
    }
}
