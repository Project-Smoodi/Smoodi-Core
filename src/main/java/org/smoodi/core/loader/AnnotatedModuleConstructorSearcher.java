package org.smoodi.core.loader;

import java.lang.reflect.Constructor;

public class AnnotatedModuleConstructorSearcher
        implements ModuleConstructorSearcher {

    @Override
    public Constructor<?> findModuleInitConstructor(Class<?> klass) {
        for (Constructor<?> constructor : klass.getConstructors()) {
            if (constructor.getAnnotation(ModuleInitConstructor.class) != null) {
                return constructor;
            }
        }

        throw new IllegalStateException(
                "Cannot found module initialization constructor in class: " + klass.getName()
                        + " with searcher: " + this.getClass().getName()
        );
    }
}
