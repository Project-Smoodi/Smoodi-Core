package org.smoodi.core.loader;

import java.lang.reflect.Constructor;

public interface ModuleInitConstructorSearcher {

    Constructor<?> findModuleInitConstructor(Class<?> klass);
}
