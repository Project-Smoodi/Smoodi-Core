package org.smoodi.core.module.loader;

import org.smoodi.annotation.UseCopy;
import org.smoodi.core.module.ModuleType;

import java.util.List;

public interface ModuleInitializer {

    @UseCopy
    void initialize(List<ModuleType<?>> moduleTypes);
}
