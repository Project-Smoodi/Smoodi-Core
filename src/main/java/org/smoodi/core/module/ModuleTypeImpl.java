package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.StaticFactoryMethod;
import org.smoodi.core.annotation.Module;
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
public final class ModuleTypeImpl<T> implements ModuleType<T> {

    @Getter
    private final Class<T> klass;

    @Getter
    private T primaryInstance;

    private Constructor<T> moduleInitConstructor;

    @Override
    public Constructor<T> getModuleInitConstructor() {
        if (!isInstantiableKlass()) {
            return null;
        }

        if (moduleInitConstructor == null) {
            this.moduleInitConstructor =
                    ModuleUtils.findModuleInitConstructor(this);
        }

        return this.moduleInitConstructor;
    }

    @Getter
    private final Set<ModuleType<? extends T>> subTypes;

    private ModuleTypeImpl(
            @NotNull Class<T> klass,
            @NotNull Set<ModuleType<? extends T>> subTypes
    ) {
        this(klass, null, subTypes);
    }

    private ModuleTypeImpl(
            @NotNull Class<T> klass,
            @Nullable Constructor<T> moduleInitConstructor,
            @NotNull Set<ModuleType<? extends T>> subTypes
    ) {
        assert klass != null;
        assert subTypes != null;

        this.klass = klass;
        this.moduleInitConstructor = moduleInitConstructor;
        this.subTypes = subTypes;

        ModuleTypeContainer.addModuleType(this);
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(@NotNull Class<T> klass) {
        assert klass != null;

        if (!ModuleType.isKlassModule(klass)) {
            throw new IllegalArgumentException(klass.getName() + " Cannot be " + ModuleTypeImpl.class.getName() + ". Maybe it doesn't have annotation "
                    + Module.class.getName());
        }

        return Nullability.firstOrSecondIfNull(
                ModuleTypeContainer.getModuleType(klass),
                () -> new ModuleTypeImpl<>(klass, ModuleUtils.getModuleSubTypes(klass))
        );
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(@NotNull T o) {
        assert o != null;

        //noinspection unchecked
        var moduleType = ModuleTypeImpl.of((Class<T>) o.getClass());
        moduleType.markAsInstanceCreated(o);
        return moduleType;
    }

    @Override
    public void markAsInstanceCreated(T primaryInstance) {
        if (this.klass.isInstance(primaryInstance)) {
            this.primaryInstance = primaryInstance;
        } else {
            throw new IllegalStateException("Cannot mark as instance created of " + this.klass);
        }
    }
}
