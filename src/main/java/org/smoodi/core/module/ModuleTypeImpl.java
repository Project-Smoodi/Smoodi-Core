package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.StaticFactoryMethod;
import org.smoodi.core.annotation.Module;
import org.smoodi.core.util.ModuleUtils;
import org.smoodi.core.util.Nullability;

import java.util.List;
import java.util.Set;

public final class ModuleTypeImpl<T> implements ModuleType<T> {

    @Getter(onMethod_ = {@Override})
    private final Class<T> klass;

    @Getter(onMethod_ = {@Override})
    private T primaryInstance;

    private ModuleInitConstructor<T, ModuleType<T>> moduleInitConstructor = null;

    @Override
    public ModuleInitConstructor<T, ModuleType<T>> getModuleInitConstructor() {
        init();
        return this.moduleInitConstructor;
    }

    private List<ModuleType<? extends T>> subTypes = null;

    @Override
    public List<ModuleType<? extends T>> getSubTypes() {
        init();
        return this.subTypes;
    }

    private boolean initialized = false;

    private synchronized void init() {
        if (initialized) {
            return;
        }

        if (this.moduleInitConstructor == null && this.isInstantiableKlass()) {
            this.moduleInitConstructor = ModuleInitConstructor.of(this);
        }

        this.subTypes = ModuleUtils.getModuleSubTypes(klass);

        initialized = true;
    }

    private ModuleTypeImpl(
            @NotNull Class<T> klass
    ) {
        assert klass != null;

        this.klass = klass;

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
                () -> new ModuleTypeImpl<>(klass)
        );
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
