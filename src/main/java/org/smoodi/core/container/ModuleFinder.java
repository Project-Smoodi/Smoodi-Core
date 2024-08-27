package org.smoodi.core.container;

import java.util.List;
import java.util.Map;

public interface ModuleFinder {

    <T> List<T> find(Map<Class<?>, List<Object>> objects, Class<T> klass);
}
