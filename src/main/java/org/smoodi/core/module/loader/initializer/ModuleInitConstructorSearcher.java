package org.smoodi.core.module.loader.initializer;

import java.lang.reflect.Constructor;

public interface ModuleInitConstructorSearcher {

    <T> Constructor<T> findModuleInitConstructor(Class<T> klass);
}
