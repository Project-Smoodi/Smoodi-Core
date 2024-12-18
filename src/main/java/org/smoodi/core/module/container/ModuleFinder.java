package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;

import java.util.Map;
import java.util.Set;

interface ModuleFinder {

    @EmptyableArray
    @NotNull
    <T> Set<T> find(@NotNull Map<ModuleType<?>, Set<Object>> objects, @NotNull ModuleType<T> moduleType);
}
