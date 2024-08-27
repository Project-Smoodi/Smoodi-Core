package org.smoodi.core.context;

import org.smoodi.core.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrimaryModuleFinder extends ReflectionBasedModuleFinder {

    @Override
    public <T> List<T> find(Map<Class<?>, List<Object>> objects, Class<T> klass) {

        final var subTypes = collectWithSubTypes(klass);

        final List<Object> found = new ArrayList<>();

        subTypes.forEach(subType -> found.addAll(
                objects.get(klass)
        ));

        if (found.size() == 1) {
            return (List<T>) found;
        }

        var primary = found.stream().filter(
                it -> it.getClass().getAnnotation(Module.class).isPrimary()).toList();

        if (primary.size() > 1) {
            throw new IllegalStateException("Many primary module found. Primary module MUST BE one.");
        } else if (primary.isEmpty()) {
            throw new IllegalStateException("Many modules found BUT the primary module does not exist.");
        }

        return (List<T>) List.of(primary);
    }
}
