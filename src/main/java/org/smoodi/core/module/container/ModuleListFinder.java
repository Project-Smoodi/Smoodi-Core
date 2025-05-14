package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.ModuleUtils;

import java.util.ArrayList;
import java.util.List;

class ModuleListFinder implements ModuleFinder {

    @EmptyableArray
    @NotNull
    @Override
    public <T> List<T> find(@NotNull Modules modules, @NotNull ModuleType<T> moduleType) {

        final List<T> found = new ArrayList<>();

        moduleType.getSubTypes().forEach(subType -> {
            if (modules.get(subType) != null) {
                found.addAll(
                        modules.get(subType)
                );
            }
        });

        found.sort(ModuleUtils.comparator());
        return found;
    }
}
