package org.smoodi.core.module.container;

import org.smoodi.core.util.AnnotationUtils;
import org.smoodi.core.annotation.Module;
import org.smoodi.core.module.ModuleDeclareError;

import java.util.*;

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
                it -> Objects.requireNonNull(
                        AnnotationUtils.findIncludeAnnotation(it.getClass(), Module.class)
                ).isPrimary()
        ).toList();

        if (primary.size() > 1) {
            throw new ModuleDeclareError("Many primary module found. Primary module MUST BE one: " + klass.getName());
        } else if (primary.isEmpty() && found.size() > 1) {
            throw new ModuleDeclareError("Many modules found BUT the primary module does not exist: " + klass.getName());
        }

        return Collections.emptyList();
    }
}
