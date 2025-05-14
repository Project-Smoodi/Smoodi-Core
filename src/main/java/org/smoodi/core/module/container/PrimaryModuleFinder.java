package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.ModuleUtils;

import java.util.Collections;
import java.util.SequencedSet;

class PrimaryModuleFinder implements ModuleFinder {

    @EmptyableArray
    @NotNull
    @Override
    public <T> SequencedSet<T> find(@NotNull ModuleSet moduleSet, @NotNull ModuleType<T> moduleType) {
        SequencedSet<T> sequenced = moduleSet.get(ModuleUtils.findPrimaryModuleType(moduleType));

        if (sequenced.isEmpty()) {
            return Collections.emptySortedSet();
        } else if (sequenced.size() > 1) {
            throw new IllegalStateException("More than one primary module found.");
        }

        return Collections.unmodifiableSequencedSet(sequenced);
    }
}
