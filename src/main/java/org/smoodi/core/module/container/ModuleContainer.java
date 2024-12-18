package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;

import java.util.Set;

public interface ModuleContainer {

    void save(@NotNull Object module);

    @Nullable
    <T> T getPrimaryModuleByClass(@NotNull Class<T> klass);

    @NotNull
    <T> Set<T> getModulesByClass(@NotNull Class<T> klass);

    @NotNull
    int getModuleCount();
}
