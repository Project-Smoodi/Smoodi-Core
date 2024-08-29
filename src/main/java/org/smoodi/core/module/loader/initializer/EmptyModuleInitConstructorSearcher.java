package org.smoodi.core.module.loader.initializer;

import org.smoodi.core.module.ModuleDeclareError;

import java.lang.reflect.Constructor;

public class EmptyModuleInitConstructorSearcher
        implements ModuleInitConstructorSearcher {

    @Override
    public Constructor<Object> findModuleInitConstructor(Class<Object> klass) {

        for (Constructor<?> constructor : klass.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return (Constructor<Object>) constructor;
            }
        }

        throw new ModuleDeclareError(
                "Cannot found module initialization constructor in class: " + klass.getName()
                        + " with searcher: " + this.getClass().getName()
        );
    }
}
