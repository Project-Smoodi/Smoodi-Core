package org.smoodi.core.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultModuleContainer extends CachedProxyModuleContainer {

    private final Map<Class<?>, List<Object>> modules = new HashMap<>();

    private final PrimaryModuleFinder pf = new PrimaryModuleFinder();
    private final ModuleListFinder lf = new ModuleListFinder();

    @Override
    public void save(Object module) {
        /* computeIfAbsent =>
        if (modules.get(module.getClass()) == null) {
            modules.put(module.getClass(), new ArrayList<>());
        }
        */
        modules.computeIfAbsent(module.getClass(), k -> new ArrayList<>());

        modules.get(module.getClass()).add(module);
    }

    @Override
    protected <T> T getPrimaryModuleByClassImpl(Class<T> klass) {
        final var found = pf.find(modules, klass);

        if (found.isEmpty()) {
            return null;
        }
        return found.getFirst();
    }

    @Override
    protected <T> List<T> getModulesByClassImpl(Class<T> klass) {
        return lf.find(modules, klass);
    }
}
