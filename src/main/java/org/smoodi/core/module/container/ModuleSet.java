package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;

import java.util.SequencedSet;
import java.util.Set;

public interface ModuleSet {

    @Nullable
    <T> SequencedSet<T> get(ModuleType<T> moduleType);

    @EmptyableArray
    @NotNull
    Set<Object> getAll();

    <T> void add(ModuleType<T> moduleType, T value);

    int size();
}
