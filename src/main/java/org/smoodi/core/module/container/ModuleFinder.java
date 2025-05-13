package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;

import java.util.SequencedSet;

interface ModuleFinder {

    @EmptyableArray
    @NotNull
    <T> SequencedSet<T> find(@NotNull ModuleSet moduleSet, @NotNull ModuleType<T> moduleType);
}
