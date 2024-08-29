package org.smoodi.core.module.loader.initializer;

import java.lang.reflect.Constructor;

public interface ModuleInitConstructorSearcher {

    Constructor<Object> findModuleInitConstructor(Class<Object> klass);
}
