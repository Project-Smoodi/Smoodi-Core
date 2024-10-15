package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.core.module.loader.initializer.DefaultModuleInitConstructorSearcher;

import java.lang.reflect.Constructor;

/**
 * <p>{@link org.smoodi.core.annotation.Module 모듈} 초기화에서 요구되는 {@link Class 클래스 정보}와 기타 추가적인 정보를 묶은 클래스.</p>
 *
 * @author Daybreak312
 * @see org.smoodi.core.annotation.Module
 * @see Class
 */
public class ModuleType {

    @Getter
    protected final Class<?> klass;

    @Getter
    protected final boolean isSingleton;

    protected Constructor<?> moduleInitConstructor;

    public Constructor<?> getModuleInitConstructor() {
        if (moduleInitConstructor == null) {
            this.moduleInitConstructor = new DefaultModuleInitConstructorSearcher().findModuleInitConstructor(klass);
        }

        return this.moduleInitConstructor;
    }

    protected ModuleType(
            Class<?> klass,
            boolean isSingleton
    ) {
        this.klass = klass;
        this.isSingleton = isSingleton;
    }

    protected ModuleType(
            Class<?> klass,
            boolean isSingleton,
            Constructor<?> moduleInitConstructor
    ) {
        this.klass = klass;
        this.isSingleton = isSingleton;
        this.moduleInitConstructor = moduleInitConstructor;
    }

    public static ModuleType of(Class<?> klass) {
        return new ModuleType(klass, true);
    }

    public static ModuleType of(Class<?> klass, boolean isSingleton) {
        return new ModuleType(klass, isSingleton);
    }

    public static ModuleType of(Constructor<?> moduleInitConstructor) {
        return new ModuleType(moduleInitConstructor.getDeclaringClass(), true, moduleInitConstructor);
    }

    public static ModuleType of(Constructor<?> moduleInitConstructor, boolean isSingleton) {
        return new ModuleType(moduleInitConstructor.getDeclaringClass(), isSingleton, moduleInitConstructor);
    }
}
