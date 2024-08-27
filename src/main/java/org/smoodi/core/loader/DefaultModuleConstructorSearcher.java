package org.smoodi.core.loader;

import java.lang.reflect.Constructor;

public class DefaultModuleConstructorSearcher
        implements ModuleConstructorSearcher {

    @Override
    public Constructor<?> findConstructor(Class<?> klass) {
        Constructor<?> emptyConstructor = null;

        for (Constructor<?> constructor : klass.getConstructors()) {
            if (constructor.getAnnotation(ModuleConstructor.class) != null) {
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
