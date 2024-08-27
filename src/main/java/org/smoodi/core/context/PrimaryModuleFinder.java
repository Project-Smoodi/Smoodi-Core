package org.smoodi.core.context;

import org.smoodi.core.Module;
import org.smoodi.core.loader.ModuleDeclareError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PrimaryModuleFinder extends ReflectionBasedModuleFinder {

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

        if (found.size() == 1) {
            return (List<T>) found;
        }

        var primary = found.stream().filter(
                it -> it.getClass().getAnnotation(Module.class).isPrimary()).toList();

        if (primary.size() > 1) {
            throw new ModuleDeclareError("Many primary module found. Primary module MUST BE one: " + klass.getName());
        } else if (primary.isEmpty() && found.size() > 1) {
            throw new ModuleDeclareError("Many modules found BUT the primary module does not exist: " + klass.getName());
        }

        return Collections.emptyList();
    }
}
