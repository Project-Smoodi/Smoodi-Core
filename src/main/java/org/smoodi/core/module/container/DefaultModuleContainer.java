package org.smoodi.core.module.container;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultModuleContainer extends CachedProxyModuleContainer {

    private final Map<Class<?>, Set<Object>> modules = new ConcurrentHashMap<>();

    private final PrimaryModuleFinder pf = new PrimaryModuleFinder();
    private final ModuleListFinder lf = new ModuleListFinder();

    @Override
    public void save(Object module) {
        modules.computeIfAbsent(module.getClass(), k -> new HashSet<>());

        modules.get(module.getClass()).add(module);
    }

    @Override
    protected <T> T getPrimaryModuleByClassImpl(Class<T> klass) {
        final var found = pf.find(modules, klass);

        if (found.isEmpty()) {
            return null;
        }
        return found.iterator().next();
    }

    @Override
    protected <T> Set<T> getModulesByClassImpl(Class<T> klass) {
        return lf.find(modules, klass);
    }

    @Override
    public int getModuleCount() {
        return modules.size();
    }
}
