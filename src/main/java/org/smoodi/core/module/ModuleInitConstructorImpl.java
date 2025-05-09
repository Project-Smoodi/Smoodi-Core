package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.core.util.ModuleUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ModuleInitConstructorImpl<T, M extends ModuleType<T>>
        implements ModuleInitConstructor<T, M> {

    @Getter(onMethod_ = {@Override})
    private final M moduleType;

    @Getter(onMethod_ = {@Override})
    private final Constructor<T> javaConstructor;

    @Getter(onMethod_ = {@Override})
    private final List<ModuleDependency<M, ?>> dependencies;

    public static <T, M extends ModuleType<T>> ModuleInitConstructor<T, M> of(M moduleType) {
        return new ModuleInitConstructorImpl<>(moduleType);
    }

    private ModuleInitConstructorImpl(M moduleType) {
        this.moduleType = moduleType;
        this.javaConstructor = ModuleUtils.findModuleInitConstructor(this.moduleType);
        this.dependencies = extractDependencies(moduleType, this.javaConstructor);
    }

    private List<ModuleDependency<M, ?>> extractDependencies(M moduleType, Constructor<T> constructor) {
        final List<ModuleDependency<M, ?>> dependencies = new ArrayList<>();

        for (Class<?> parameterType : constructor.getParameterTypes()) {
            dependencies.add(
                    ModuleDependencyImpl.of(moduleType, parameterType)
            );
        }

        return List.copyOf(dependencies);
    }
}
