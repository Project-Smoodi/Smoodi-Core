package org.smoodi.core.module;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;

/**
 * <p>어떠한 {@link ModuleType}의 의존성</p>
 *
 * @param <T> 이 의존성을 가지고 있는 {@link ModuleType}
 * @param <D> 생성자에 명시된 원본 타입의 {@link ModuleType}
 * @author Daybreak312
 * @see ModuleType
 * @see ModuleInitConstructor
 * @since 0.0.5-SNAPSHOT
 */
public interface ModuleDependency<T extends ModuleType<?>, D> {

    @NotNull
    T getModuleType();

    @NotNull
    D getDependencyType();

    @Nullable
    ModuleType<? extends D> getModuleTypeForInjection();
}
