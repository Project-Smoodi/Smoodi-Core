package org.smoodi.core.context;

import java.util.List;

public interface ModuleContainer {

    void save(Object module);

    <T> T getPrimaryModuleByClass(Class<T> klass);

    <T> List<T> getModulesByClass(Class<T> klass);

    static ModuleContainer getInstance() {
        return new DefaultModuleContainer();
    }

    ;
}
