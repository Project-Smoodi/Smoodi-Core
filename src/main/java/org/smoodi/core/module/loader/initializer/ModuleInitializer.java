package org.smoodi.core.module.loader.initializer;

import java.util.List;

public interface ModuleInitializer {

    void initialize(List<Class<?>> moduleClasses);
}
