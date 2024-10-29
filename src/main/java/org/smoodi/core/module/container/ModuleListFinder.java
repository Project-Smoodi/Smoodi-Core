package org.smoodi.core.module.container;

import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.ModuleUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ModuleListFinder implements ModuleFinder {

    @Override
    public <T> Set<T> find(Map<ModuleType<?>, Set<Object>> objects, ModuleType<T> moduleType) {

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
