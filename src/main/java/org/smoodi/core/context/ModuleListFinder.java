package org.smoodi.core.context;

import java.util.List;
import java.util.Map;

public class ModuleListFinder extends ReflectionBasedModuleFinder {

    @Override
    public <T> List<T> find(Map<Class<?>, List<Object>> objects, Class<T> klass) {
        var found = collectWithSubTypes(klass);

        return (List<T>) found;
    }
}
