package org.smoodi.core.module.loader;

import org.smoodi.core.module.ModuleType;

import java.util.List;

public interface ModuleClassScanner {

    List<ModuleType<?>> getModuleClasses(String basePackage);
}
