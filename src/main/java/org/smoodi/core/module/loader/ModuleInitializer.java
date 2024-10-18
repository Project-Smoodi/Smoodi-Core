package org.smoodi.core.module.loader;

import org.smoodi.core.module.ModuleType;

import java.util.Set;

public interface ModuleInitializer {

    void initialize(Set<ModuleType<?>> moduleTypes);
}
