package org.smoodi.core.module.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.annotation.StaticModule;
import org.smoodi.core.module.ModuleType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@StaticModule
public class DefaultModuleContainer extends CachedProxyModuleContainer {

    private static final Logger log = LoggerFactory.getLogger(DefaultModuleContainer.class);
    private final Map<ModuleType<?>, Set<Object>> modules = new ConcurrentHashMap<>();

    private final PrimaryModuleFinder pf = new PrimaryModuleFinder();
    private final ModuleListFinder lf = new ModuleListFinder();

    @Override
    public void save(@NotNull Object module) {
        assert module != null;

        modules.computeIfAbsent(ModuleType.of(module.getClass()), k -> new HashSet<>());

        modules.get(ModuleType.of(module.getClass())).add(module);

        //noinspection unchecked
        ModuleType.of((Class<Object>) module.getClass()).markAsInstanceCreated(module);
    }

    @Nullable
    @Override
    protected <T> T getPrimaryModuleByClassImpl(Class<T> klass) {
        final var found = pf.find(modules, ModuleType.of(klass));

        if (found.isEmpty()) {
            return null;
        }
        return found.iterator().next();
    }

    @EmptyableArray
    @NotNull
    @Override
    protected <T> Set<T> getModulesByClassImpl(Class<T> klass) {
        return lf.find(modules, ModuleType.of(klass));
    }

    @Override
    public int getModuleCount() {
        return modules.size();
    }
}
