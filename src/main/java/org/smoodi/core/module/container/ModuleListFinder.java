package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.UtilCollection;

import java.util.List;

class ModuleListFinder implements ModuleFinder {

    @EmptyableArray
    @NotNull
    @Override
    public <T> List<T> find(@NotNull Modules modules, @NotNull ModuleType<T> moduleType) {

        final List<T> found = UtilCollection.SortedList.ofModule();

        moduleType.getSubTypes().forEach(subType -> {
            if (modules.get(subType) != null) {
                found.addAll(
                        modules.get(subType)
                );
            }
        });

        return found;
    }
}
