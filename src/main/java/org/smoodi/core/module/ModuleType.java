package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.StaticFactoryMethod;
import org.smoodi.core.module.loader.initializer.DefaultModuleInitConstructorSearcher;
import org.smoodi.core.util.Nullability;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * <p>{@link org.smoodi.core.annotation.Module 모듈} 초기화에서 요구되는 {@link Class 클래스 정보}와 기타 추가적인 정보를 묶은 클래스.</p>
 *
 * @author Daybreak312
 * @see org.smoodi.core.annotation.Module
 * @see Class
 */
public class ModuleType<T> {

    @Getter
    protected final Class<T> klass;

    @Getter
    protected final boolean isCreatable;

    protected Constructor<T> moduleInitConstructor;

    public Constructor<T> getModuleInitConstructor() {
        if (moduleInitConstructor == null) {
            this.moduleInitConstructor = new DefaultModuleInitConstructorSearcher().findModuleInitConstructor(klass);
        }

        return this.moduleInitConstructor;
    }

    private final Set<ModuleType<? extends T>> lowerTypes;

    @Getter
    private int initializedLowerModuleTypes = 0;

    protected ModuleType(
            Class<T> klass,
            Set<ModuleType<? extends T>> lowerTypes
    ) {
        assert klass != null;
        assert lowerTypes != null;

        this.klass = klass;
        this.isCreatable = isCreatableKlass(klass);
        this.lowerTypes = lowerTypes;
    }

    protected ModuleType(
            Class<T> klass,
            Constructor<T> moduleInitConstructor,
            Set<ModuleType<? extends T>> lowerTypes
    ) {
        assert klass != null;
        assert moduleInitConstructor != null;
        assert lowerTypes != null;

        this.klass = klass;
        this.isCreatable = isCreatableKlass(klass);
        this.moduleInitConstructor = moduleInitConstructor;
        this.lowerTypes = lowerTypes;
    }

    private boolean isCreatableKlass(Class<T> klass) {
        return !klass.isAnnotation() && !klass.isEnum() && !klass.isInterface() && !klass.isPrimitive() && !klass.isAnonymousClass();
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(Class<T> klass, Set<ModuleType<? extends T>> lowerTypes) {
        return Nullability.firstOrSecondIfNull(
                ModuleTypeContainer.getModuleType(klass),
                new ModuleType<>(klass, lowerTypes)
        );
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(Constructor<T> moduleInitConstructor, Set<ModuleType<? extends T>> lowerTypes) {
        return Nullability.firstOrSecondIfNull(
                ModuleTypeContainer.getModuleType(moduleInitConstructor.getDeclaringClass()),
                new ModuleType<>(
                        moduleInitConstructor.getDeclaringClass(),
                        moduleInitConstructor,
                        lowerTypes
                )
        );
    }

    public void markInit(ModuleType<? extends T> type) {
        if (this.lowerTypes.contains(type)) {
            this.initializedLowerModuleTypes++;
        }
    }
}
