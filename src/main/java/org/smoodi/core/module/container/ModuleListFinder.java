package org.smoodi.core.module.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModuleListFinder extends ReflectionBasedModuleFinder {

    @Override
    public <T> List<T> find(Map<Class<?>, List<Object>> objects, Class<T> klass) {
        final var subTypes = collectWithSubTypes(klass);

        final List<Object> found = new ArrayList<>();

        subTypes.forEach(subType -> {
            if (objects.get(subType) != null) {
                found.addAll(
                        objects.get(subType)
                );
            }
        });


        return (List<T>) found;
    }
}
