package org.smoodi.core.module.loader.initializer;

import org.smoodi.core.annotation.ModuleInitConstructor;
import org.smoodi.core.module.ModuleDeclareError;

import java.lang.reflect.Constructor;

public class DefaultModuleInitConstructorSearcher
        implements ModuleInitConstructorSearcher {

    @SuppressWarnings("unchecked")
    @Override
    public <T> Constructor<T> findModuleInitConstructor(Class<T> klass) {
        Constructor<T> emptyConstructor = null;

        if (klass.getConstructors().length == 1) {
            return (Constructor<T>) klass.getConstructors()[0];
        }

        for (Constructor<?> constructor : klass.getConstructors()) {
            if (constructor.getAnnotation(ModuleInitConstructor.class) != null) {
                return (Constructor<T>) constructor;
            }

            if (constructor.getParameterCount() == 0) {
                emptyConstructor = (Constructor<T>) constructor;
            }
        }

        if (emptyConstructor != null) {
            return emptyConstructor;
        }

        throw new ModuleDeclareError(
                "Cannot found module initialization constructor in class: " + klass.getName()
                        + " with searcher: " + this.getClass().getName()
        );
    }
}
