package org.smoodi.core.module.loader;

import java.lang.reflect.Constructor;

public interface ModuleInitConstructorSearcher {

    Constructor<?> findModuleInitConstructor(Class<?> klass);
}
