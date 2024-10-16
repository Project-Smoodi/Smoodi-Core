package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.StaticFactoryMethod;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * <p>상속 관계 정보를 포함한 {@link ModuleType}.</p>
 *
 * <p>특정 타입에 대해 대응 가능한 모든 {@link org.smoodi.core.annotation.Module 모듈}이 모두 초기화되었는지 확인하기 위해 사용.</p>
 *
 * @author Daybreak312
 * @see org.smoodi.core.annotation.Module
 * @see Class
 * @see ModuleType
 */
public final class HierarchyModuleType<T> extends ModuleType<T> {

    private final Set<ModuleType<? extends T>> lowerTypes;

    @Getter
    private int initializedLowerModuleTypes = 0;

    private HierarchyModuleType(
            Class<T> klass,
            boolean isSingleton,
            Set<ModuleType<? extends T>> lowerTypes
    ) {
        super(klass, isSingleton);
        this.lowerTypes = lowerTypes;
    }

    private HierarchyModuleType(
            Class<T> klass,
            boolean isSingleton,
            Constructor<T> moduleInitConstructor,
            Set<ModuleType<? extends T>> lowerTypes
    ) {
        super(klass, isSingleton, moduleInitConstructor);
        this.lowerTypes = lowerTypes;
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> HierarchyModuleType<T> of(Class<T> klass) {
        return new HierarchyModuleType<>(klass, false, null);
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> HierarchyModuleType<T> of(Class<T> klass, boolean isSingleton) {
        return new HierarchyModuleType<>(klass, isSingleton, null);
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> HierarchyModuleType<T> of(Constructor<T> moduleInitConstructor) {
        return new HierarchyModuleType<>(moduleInitConstructor.getDeclaringClass(), true, moduleInitConstructor, null);
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> HierarchyModuleType<T> of(Constructor<T> moduleInitConstructor, boolean isSingleton) {
        return new HierarchyModuleType<>(moduleInitConstructor.getDeclaringClass(), isSingleton, moduleInitConstructor, null);
    }

    public void markInit(ModuleType<? extends T> type) {
        if (this.lowerTypes.contains(type)) {
            this.initializedLowerModuleTypes++;
        }
    }
}
