package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.annotation.StaticModule;
import org.smoodi.core.module.ModuleType;

import java.util.SequencedSet;

@StaticModule
public class DefaultModuleContainer extends CachedProxyModuleContainer {

    private final ModuleSet moduleSet = new TreeSetModuleSet();

    private final PrimaryModuleFinder pf = new PrimaryModuleFinder();
    private final ModuleListFinder lf = new ModuleListFinder();

    @Override
    public void save(@NotNull final Object module) {
        assert module != null;

        // 'module.getClass()' return 'Class<? extends Object>', so it is available cast.
        //noinspection unchecked
        moduleSet.add((ModuleType<Object>) ModuleType.of(module.getClass()), module);

        //noinspection unchecked
        ModuleType.of((Class<Object>) module.getClass()).markAsInstanceCreated(module);
    }

    @Nullable
    @Override
    protected <T> T getPrimaryModuleByClassImpl(Class<T> klass) {
        final var found = pf.find(moduleSet, ModuleType.of(klass));

        if (found.isEmpty()) {
            return null;
        }
        return found.iterator().next();
    }

    @EmptyableArray
    @NotNull
    @Override
    protected <T> SequencedSet<T> getModulesByClassImpl(Class<T> klass) {
        return lf.find(moduleSet, ModuleType.of(klass));
    }

    @Override
    public int getModuleCount() {
        return moduleSet.size();
    }
}
