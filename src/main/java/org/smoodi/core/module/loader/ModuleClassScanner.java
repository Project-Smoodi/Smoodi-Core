package org.smoodi.core.module.loader;

import java.util.Set;

public interface ModuleClassScanner {

    Set<Class<?>> getModuleClasses(String basePackage);
}
