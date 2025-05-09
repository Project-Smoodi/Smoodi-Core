package org.smoodi.core.module;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.annotation.array.UnmodifiableArray;

import java.lang.reflect.Constructor;
import java.util.Set;

public interface ModuleInitConstructor<T extends ModuleType<?>> {

    @NotNull
    T getModuleType();

    @NotNull
    Constructor<T> getJavaConstructor();

    @NotNull
    @EmptyableArray
    @UnmodifiableArray
    Set<ModuleDependency<T, ?>> getDependencies();
}
