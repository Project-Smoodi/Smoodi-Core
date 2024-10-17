package org.smoodi.core.module.container;

import org.smoodi.core.module.ModuleType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModuleListFinder implements ModuleFinder {

    @Override
    public <T> Set<T> find(Map<ModuleType<?>, Set<Object>> objects, ModuleType<T> moduleType) {

        final Set<T> found = new HashSet<>();

        moduleType.getSubTypes().forEach(subType -> {
            if (objects.get(subType) != null) {
                //noinspection unchecked
                found.addAll(
                        (List<? extends T>) objects.get(subType)
                );
            }
        });

        return found;
    }
}
