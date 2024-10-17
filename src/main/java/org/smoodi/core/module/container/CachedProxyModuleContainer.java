package org.smoodi.core.module.container;

import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class CachedProxyModuleContainer
        implements ModuleContainer {

    private final HashMap<Class<?>, Set<Object>> listCache = new HashMap<>();

    private final HashMap<Class<?>, Object> primaryCache = new HashMap<>();


    @Override
    public final <T> T getPrimaryModuleByClass(Class<T> klass) {
        if (primaryCache.get(klass) != null) {
            return (T) primaryCache.get(klass);
        }

        var found = getPrimaryModuleByClassImpl(klass);

        primaryCache.put(klass, found);
        return found;
    }

    protected abstract <T> T getPrimaryModuleByClassImpl(Class<T> klass);

    @Override
    public final <T> Set<T> getModulesByClass(Class<T> klass) {
        if (listCache.get(klass) != null) {
            return (Set<T>) listCache.get(klass);
        }

        var found = getModulesByClassImpl(klass);

        listCache.put(klass, (Set<Object>) found);
        return found;
    }

    protected abstract <T> Set<T> getModulesByClassImpl(Class<T> klass);
}
