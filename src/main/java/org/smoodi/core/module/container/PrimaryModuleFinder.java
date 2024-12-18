package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.annotation.Module;
import org.smoodi.core.module.ModuleDeclareError;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.AnnotationUtils;

import java.util.*;

class PrimaryModuleFinder implements ModuleFinder {

    @EmptyableArray
    @NotNull
    @Override
    public <T> Set<T> find(@NotNull Map<ModuleType<?>, Set<Object>> objects, @NotNull ModuleType<T> moduleType) {

        final Set<Object> found = new HashSet<>();

        final var subTypes = new ArrayList<>(moduleType.getSubTypes());
        subTypes.add(moduleType);

        subTypes.forEach(subType -> {
            if (objects.get(subType) != null) {
                found.addAll(
                        objects.get(subType)
                );
            }
        });

        if (found.size() == 1) {
            //noinspection unchecked
            return (Set<T>) Collections.singleton(found.iterator().next());
        }

        var primary = found.stream().filter(
                it -> Objects.requireNonNull(
                        AnnotationUtils.findIncludeAnnotation(it.getClass(), Module.class)
                ).isPrimary()
        ).toList();

        if (primary.size() > 1) {
            throw new ModuleDeclareError("Many primary module found. Primary module MUST BE one: " + moduleType.getKlass().getName());
        } else if (primary.isEmpty() && found.size() > 1) {
            throw new ModuleDeclareError("Many modules found BUT the primary module does not exist: " + moduleType.getKlass().getName());
        } else if (primary.isEmpty() /* && found.isEmpty() => true */) {
            return Collections.emptySet();
        }

        //noinspection unchecked
        return (Set<T>) Collections.singleton(found.iterator().next());
    }
}
