package org.smoodi.core.module.container;

import org.smoodi.core.module.ModuleType;

import java.util.Map;
import java.util.Set;

public interface ModuleFinder {

    <T> Set<T> find(Map<ModuleType<?>, Set<Object>> objects, ModuleType<T> moduleType);
}
