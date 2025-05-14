package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.array.EmptyableArray;

import java.util.List;
import java.util.function.Predicate;

public interface ModuleContainer {

    void save(@NotNull Object module);

    @Nullable
    <T> T getPrimaryModuleByClass(@NotNull Class<T> klass);

    @EmptyableArray
    @NotNull
    <T> List<T> getModulesByClass(@NotNull Class<T> klass);

    @NotNull
    int getModuleCount();

    @NotNull
    List<?> filter(@NotNull Predicate<Object> function);
}
