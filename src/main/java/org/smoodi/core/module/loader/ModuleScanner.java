package org.smoodi.core.module.loader;

import java.util.Set;

public interface ModuleScanner {

    Set<String> getModuleClassNames(String basePackage);
}
