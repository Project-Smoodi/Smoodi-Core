package org.smoodi.core.module.container;

import java.util.Map;
import java.util.Set;

public interface ModuleFinder {

    <T> Set<T> find(Map<Class<?>, Set<Object>> objects, Class<T> klass);
}
