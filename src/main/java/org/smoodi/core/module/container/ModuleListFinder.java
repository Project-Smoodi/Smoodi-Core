package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.ModuleUtils;

import java.util.HashSet;
import java.util.SequencedSet;
import java.util.Set;
import java.util.TreeSet;

class ModuleListFinder implements ModuleFinder {

    @EmptyableArray
    @NotNull
    @Override
    public <T> SequencedSet<T> find(@NotNull ModuleSet moduleSet, @NotNull ModuleType<T> moduleType) {

        final Set<T> found = new HashSet<>();

        moduleType.getSubTypes().forEach(subType -> {
            if (moduleSet.get(subType) != null) {
                found.addAll(
                        moduleSet.get(subType)
                );
            }
        });

        var toReturn = new TreeSet<T>(ModuleUtils.comparator());
        toReturn.addAll(found);

        return toReturn;
    }
}
