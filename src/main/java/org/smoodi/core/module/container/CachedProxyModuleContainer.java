package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.array.EmptyableArray;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class CachedProxyModuleContainer
        implements ModuleContainer {

    private final HashMap<Class<?>, List<?>> listCache = new HashMap<>();

    private final HashMap<Class<?>, Object> primaryCache = new HashMap<>();

    @Nullable
    @Override
    public final <T> T getPrimaryModuleByClass(@NotNull Class<T> klass) {
        assert klass != null;

        if (primaryCache.get(klass) != null) {
            return (T) primaryCache.get(klass);
        }

        var found = getPrimaryModuleByClassImpl(klass);

        primaryCache.put(klass, found);
        return found;
    }

    @Nullable
    protected abstract <T> T getPrimaryModuleByClassImpl(@NotNull Class<T> klass);

    @EmptyableArray
    @NotNull
    @Override
    public final <T> List<T> getModulesByClass(@NotNull Class<T> klass) {
        assert klass != null;

        if (listCache.get(klass) != null) {
            return (List<T>) listCache.get(klass);
        }

        var found = getModulesByClassImpl(klass);

        listCache.put(klass, found);
        return found;
    }

    @EmptyableArray
    @NotNull
    protected abstract <T> List<T> getModulesByClassImpl(@NotNull Class<T> klass);
}
