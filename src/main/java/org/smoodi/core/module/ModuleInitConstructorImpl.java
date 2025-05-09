package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.core.util.ModuleUtils;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleInitConstructorImpl<T, M extends ModuleType<T>>
        implements ModuleInitConstructor<T, M> {

    @Getter(onMethod_ = {@Override})
    private final M moduleType;

    @Getter(onMethod_ = {@Override})
    private final Constructor<T> javaConstructor;

    @Getter(onMethod_ = {@Override})
    private final Set<ModuleDependency<M, ?>> dependencies;

    public ModuleInitConstructorImpl(M moduleType) {
        this.moduleType = moduleType;
        this.javaConstructor = ModuleUtils.findModuleInitConstructor(this.moduleType);
        this.dependencies = extractDependencies(moduleType, this.javaConstructor);
    }

    private Set<ModuleDependency<M, ?>> extractDependencies(M moduleType, Constructor<T> constructor) {
        final Set<ModuleDependency<M, ?>> dependencies = new HashSet<>();

        for (Class<?> parameterType : constructor.getParameterTypes()) {
            dependencies.add(
                    ModuleDependencyImpl.of(moduleType, parameterType)
            );
        }

        return dependencies.stream().collect(Collectors.toUnmodifiableSet());
    }
}
