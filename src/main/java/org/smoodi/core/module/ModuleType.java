package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.StaticFactoryMethod;
import org.smoodi.core.annotation.IoC;
import org.smoodi.core.annotation.Module;
import org.smoodi.core.module.loader.initializer.ModuleInitConstructorSearcher;
import org.smoodi.core.util.AnnotationUtils;
import org.smoodi.core.util.ModuleUtils;
import org.smoodi.core.util.Nullability;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
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
    private T primaryInstance;

    private Constructor<T> moduleInitConstructor;

    public Constructor<T> getModuleInitConstructor() {
        if (!this.isCreatable) {
            return null;
        }

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

    private static boolean isCreatableKlass(@NotNull Class<?> klass) {
        return !klass.isAnnotation() &&
                !klass.isEnum() && !klass.isInterface() &&
                !klass.isPrimitive() && !klass.isAnonymousClass() &&
                !Modifier.toString(klass.getModifiers()).contains("abstract");
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(@NotNull Class<T> klass) {
        assert klass != null;

        if (!canBeModuleTypeKlass(klass)) {
            throw new IllegalArgumentException(klass.getSimpleName() + " Cannot be " + ModuleType.class.getSimpleName() + ". Maybe it doesn't have annotation "
                    + Module.class.getSimpleName());
        }

        return Nullability.firstOrSecondIfNull(
                ModuleTypeContainer.getModuleType(klass),
                () -> new ModuleType<>(klass, ModuleUtils.getModuleSubTypes(klass))
        );
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(@NotNull T o) {
        assert o != null;

        //noinspection unchecked
        var moduleType = ModuleType.of((Class<T>) o.getClass());
        moduleType.markAsInstanceCreated(o);
        return moduleType;
    }

    private static boolean canBeModuleTypeKlass(@NotNull Class<?> klass) {
        if (isCreatableKlass(klass)) {
            return AnnotationUtils.findIncludeAnnotation(klass, IoC.class) != null;
        } else {
            return true;
        }
    }

    public void markAsInstanceCreated(T primaryInstance) {
        if (this.klass.isInstance(primaryInstance)) {
            this.primaryInstance = primaryInstance;
        } else {
            throw new IllegalStateException("Cannot mark as instance created of " + this.klass);
        }
    }
}
