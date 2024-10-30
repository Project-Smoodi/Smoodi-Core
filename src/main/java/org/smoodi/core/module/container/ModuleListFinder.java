package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.ModuleUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

class ModuleListFinder implements ModuleFinder {

    @EmptyableArray
    @NotNull
    @Override
    public <T> Set<T> find(@NotNull Map<ModuleType<?>, Set<Object>> objects, @NotNull ModuleType<T> moduleType) {

        final Set<T> found = new HashSet<>();

        moduleType.getSubTypes().forEach(subType -> {
            if (objects.get(subType) != null) {
                //noinspection unchecked
                found.addAll(
                        (Set<? extends T>) objects.get(subType)
                );
            }
        });

        var toReturn = new TreeSet<>(ModuleUtils.comparator(moduleType.getKlass()));
        toReturn.addAll(found);

        return toReturn;
    }
}
