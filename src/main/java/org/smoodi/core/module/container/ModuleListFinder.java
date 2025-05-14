package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.ModuleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SequencedSet;
import java.util.TreeSet;

class ModuleListFinder implements ModuleFinder {

    @EmptyableArray
    @NotNull
    @Override
    public <T> SequencedSet<T> find(@NotNull ModuleSet moduleSet, @NotNull ModuleType<T> moduleType) {

        final List<T> found = new ArrayList<>();

        moduleType.getSubTypes().forEach(subType -> {
            if (moduleSet.get(subType) != null) {
                found.addAll(
                        moduleSet.get(subType)
                );
            }
        });

        TreeSet<T> toReturn = new TreeSet<>(ModuleUtils.comparator());
        toReturn.addAll(found);

        return toReturn;
    }
}
