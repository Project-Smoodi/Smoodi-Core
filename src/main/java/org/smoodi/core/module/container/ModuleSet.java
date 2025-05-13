package org.smoodi.core.module.container;

import org.smoodi.annotation.Nullable;
import org.smoodi.core.module.ModuleType;

import java.util.SequencedSet;

public interface ModuleSet {

    @Nullable
    <T> SequencedSet<T> get(ModuleType<T> moduleType);

    <T> void add(ModuleType<T> moduleType, T value);

    int size();
}
