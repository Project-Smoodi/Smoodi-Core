package org.smoodi.core.loader;

import java.lang.reflect.Constructor;

public class EmptyModuleConstructorSearcher
        implements ModuleConstructorSearcher {

    @Override
    public Constructor<?> findConstructor(Class<?> klass) {

        for (Constructor<?> constructor : klass.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }

        throw new IllegalStateException(
                "Cannot found module initialization constructor in class: " + klass.getName()
                        + " with searcher: " + this.getClass().getName()
        );
    }
}
