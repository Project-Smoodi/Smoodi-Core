package org.smoodi.core.module.container;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.core.annotation.StaticModule;
import org.smoodi.core.module.ModuleType;

import java.util.List;
import java.util.function.Predicate;

@StaticModule
public class DefaultModuleContainer extends CachedProxyModuleContainer {

    private final Modules modules = new ModulesImpl();

    private final PrimaryModuleFinder pf = new PrimaryModuleFinder();
    private final ModuleListFinder lf = new ModuleListFinder();

    @Override
    public void save(@NotNull final Object module) {
        assert module != null;

        // 'module.getClass()' return 'Class<? extends Object>', so it is available cast.
        //noinspection unchecked
        modules.add((ModuleType<Object>) ModuleType.of(module.getClass()), module);

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
        return found.getFirst();
    }

    @EmptyableArray
    @NotNull
    @Override
    protected <T> List<T> getModulesByClassImpl(Class<T> klass) {
        return lf.find(modules, ModuleType.of(klass));
    }

    @Override
    public int getModuleCount() {
        return modules.size();
    }

    @EmptyableArray
    @NotNull
    @Override
    public List<?> filter(Predicate<Object> function) {
        return modules.getAll().stream().filter(function).toList();
    }
}
