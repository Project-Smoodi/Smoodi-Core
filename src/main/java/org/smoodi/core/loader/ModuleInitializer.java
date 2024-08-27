package org.smoodi.core.loader;

import java.util.List;

public interface ModuleInitializer {

    void initialize(List<String> moduleNames);

    static ModuleInitializer getInstance() {
        return DefaultModuleInitializer.getInstance();
    }
}
