package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.StaticFactoryMethod;
import org.smoodi.core.module.loader.initializer.ModuleInitConstructorSearcher;
import org.smoodi.core.util.ModuleUtils;
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
public final class ModuleType<T> {

    @Getter
    private final Class<T> klass;

    @Getter
    private final boolean isCreatable;

    @Getter
    private T singletonInstance;

    private Constructor<T> moduleInitConstructor;

    public Constructor<T> getModuleInitConstructor() {
        if (moduleInitConstructor == null) {
            this.moduleInitConstructor =
                    ModuleInitConstructorSearcher.findModuleInitConstructor(this);
        }

        return this.moduleInitConstructor;
    }

    @Getter
    private final Set<ModuleType<? extends T>> subTypes;

    private ModuleType(
            @NotNull Class<T> klass,
            @NotNull Set<ModuleType<? extends T>> subTypes
    ) {
        this(klass, null, subTypes);
    }

    private ModuleType(
            @NotNull Class<T> klass,
            @Nullable Constructor<T> moduleInitConstructor,
            @NotNull Set<ModuleType<? extends T>> subTypes
    ) {
        assert klass != null;
        assert subTypes != null;

        this.klass = klass;
        this.isCreatable = isCreatableKlass(klass);
        this.moduleInitConstructor = moduleInitConstructor;
        this.subTypes = subTypes;

        ModuleTypeContainer.addModuleType(this);
    }

    private boolean isCreatableKlass(@NotNull Class<T> klass) {
        return !klass.isAnnotation() && !klass.isEnum() && !klass.isInterface() && !klass.isPrimitive() && !klass.isAnonymousClass();
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(@NotNull Class<T> klass) {
        assert klass != null;

        return Nullability.firstOrSecondIfNull(
                ModuleTypeContainer.getModuleType(klass),
                () -> new ModuleType<>(klass, ModuleUtils.getModuleSubTypes(klass))
        );
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(@NotNull Constructor<T> moduleInitConstructor) {
        assert moduleInitConstructor != null;

        final var klass = moduleInitConstructor.getDeclaringClass();
        return Nullability.firstOrSecondIfNull(
                ModuleTypeContainer.getModuleType(klass),
                () -> new ModuleType<>(klass, ModuleUtils.getModuleSubTypes(klass))
        );
    }

    public void markAsInstanceCreated(T primaryInstance) {
        if (this.klass.isInstance(primaryInstance)) {
            this.singletonInstance = primaryInstance;
        } else {
            throw new IllegalStateException("Cannot mark as instance created of " + this.klass);
        }
    }
}
