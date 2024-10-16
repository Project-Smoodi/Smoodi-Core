package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.StaticFactoryMethod;
import org.smoodi.core.module.loader.initializer.DefaultModuleInitConstructorSearcher;

import java.lang.reflect.Constructor;

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
    protected final boolean isSingleton;

    protected Constructor<T> moduleInitConstructor;

    public Constructor<T> getModuleInitConstructor() {
        if (moduleInitConstructor == null) {
            this.moduleInitConstructor = new DefaultModuleInitConstructorSearcher().findModuleInitConstructor(klass);
        }

        return this.moduleInitConstructor;
    }

    protected ModuleType(
            Class<T> klass,
            boolean isSingleton
    ) {
        this.klass = klass;
        this.isSingleton = isSingleton;
    }

    protected ModuleType(
            Class<T> klass,
            boolean isSingleton,
            Constructor<T> moduleInitConstructor
    ) {
        this.klass = klass;
        this.isSingleton = isSingleton;
        this.moduleInitConstructor = moduleInitConstructor;
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(Class<T> klass) {
        return new ModuleType<>(klass, true);
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(Class<T> klass, boolean isSingleton) {
        return new ModuleType<>(klass, isSingleton);
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(Constructor<T> moduleInitConstructor) {
        return new ModuleType<>(moduleInitConstructor.getDeclaringClass(), true, moduleInitConstructor);
    }

    @StaticFactoryMethod
    @NotNull
    public static <T> ModuleType<T> of(Constructor<T> moduleInitConstructor, boolean isSingleton) {
        return new ModuleType<>(moduleInitConstructor.getDeclaringClass(), isSingleton, moduleInitConstructor);
    }
}
