package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.array.EmptyableArray;

import java.util.SequencedSet;
import java.util.Set;
import java.util.function.Predicate;

public interface ModuleContainer {

    void save(@NotNull Object module);

    @Nullable
    <T> T getPrimaryModuleByClass(@NotNull Class<T> klass);

    @EmptyableArray
    @NotNull
    <T> SequencedSet<T> getModulesByClass(@NotNull Class<T> klass);

    @NotNull
    int getModuleCount();

    @NotNull
    Set<?> filter(@NotNull Predicate<Object> function);
}
