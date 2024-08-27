package org.smoodi.core.loader;

import java.lang.reflect.Constructor;

public class DefaultModuleInitConstructorSearcher
        implements ModuleInitConstructorSearcher {

    @Override
    public Constructor<?> findModuleInitConstructor(Class<?> klass) {
        Constructor<?> emptyConstructor = null;

        if (klass.getConstructors().length == 1) {
            return klass.getConstructors()[0];
        }

        for (Constructor<?> constructor : klass.getConstructors()) {
            if (constructor.getAnnotation(ModuleInitConstructor.class) != null) {
                return constructor;
            }

            if (constructor.getParameterCount() == 0) {
                emptyConstructor = constructor;
            }
        }

        if (emptyConstructor != null) {
            return emptyConstructor;
        }

        throw new IllegalStateException(
                "Cannot found module initialization constructor in class: " + klass.getName()
                        + " with searcher: " + this.getClass().getName()
        );
    }
}
