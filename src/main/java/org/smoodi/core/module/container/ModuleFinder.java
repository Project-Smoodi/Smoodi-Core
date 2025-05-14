package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.module.ModuleType;

import java.util.List;

interface ModuleFinder {

    @EmptyableArray
    @NotNull
    <T> List<T> find(@NotNull Modules modules, @NotNull ModuleType<T> moduleType);
}
