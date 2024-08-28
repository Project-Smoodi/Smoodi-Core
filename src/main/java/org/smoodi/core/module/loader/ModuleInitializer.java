package org.smoodi.core.module.loader;

import java.util.List;

public interface ModuleInitializer {

    void initialize(List<String> moduleNames);
}
