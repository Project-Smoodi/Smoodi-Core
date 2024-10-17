package org.smoodi.core.module.loader;

import org.smoodi.core.module.ModuleType;

import java.util.Set;

public interface ModuleClassScanner {

    Set<ModuleType<?>> getModuleClasses(String basePackage);
}
