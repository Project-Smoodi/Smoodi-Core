package org.smoodi.core.module.loader;

import org.smoodi.annotation.UseCopy;
import org.smoodi.core.module.ModuleType;

import java.util.Set;

public interface ModuleInitializer {

    @UseCopy
    void initialize(Set<ModuleType<?>> moduleTypes);
}
