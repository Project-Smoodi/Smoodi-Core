package org.smoodi.core.module;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModuleTypeContainer {

    private static final Map<Class<?>, ModuleType<?>> moduleTypeMap =
            new HashMap<>();

    static void addModuleType(@NotNull final ModuleType<?> moduleType) {
        assert moduleType != null;
        moduleTypeMap.put(moduleType.getKlass(), moduleType);
    }

    @Nullable
    static <T> ModuleType<T> getModuleType(@NotNull final Class<T> klass) {
        assert klass != null;
        //noinspection unchecked
        return (ModuleType<T>) moduleTypeMap.get(klass);
    }
}
