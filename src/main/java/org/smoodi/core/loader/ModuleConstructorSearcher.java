package org.smoodi.core.loader;

import java.lang.reflect.Constructor;

public interface ModuleConstructorSearcher {

    Constructor<?> findModuleInitConstructor(Class<?> klass);
}
