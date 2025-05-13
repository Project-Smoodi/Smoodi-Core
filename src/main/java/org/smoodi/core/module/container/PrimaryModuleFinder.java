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
    public <T> SequencedSet<T> find(@NotNull ModuleSet moduleSet, @NotNull ModuleType<T> moduleType) {

        final Set<T> found = new HashSet<>();

        final List<ModuleType<? extends T>> subTypes = new ArrayList<>(moduleType.getSubTypes());
        subTypes.add(moduleType);

        subTypes.forEach(subType -> {
            if (moduleSet.get(subType) != null) {
                found.addAll(
                        moduleSet.get(subType)
                );
            }
        });

        if (found.size() == 1) {
            return Collections.unmodifiableSequencedSet(
                    new TreeSet<>(found)
            );
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
            return Collections.emptySortedSet();
        }

        return  Collections.unmodifiableSequencedSet(
                new TreeSet<>(found)
        );
    }
}
