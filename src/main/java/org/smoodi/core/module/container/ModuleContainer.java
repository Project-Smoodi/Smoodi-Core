package org.smoodi.core.module.container;

import java.util.Set;

public interface ModuleContainer {

    void save(Object module);

    <T> T getPrimaryModuleByClass(Class<T> klass);

    <T> Set<T> getModulesByClass(Class<T> klass);

    int getModuleCount();
}
