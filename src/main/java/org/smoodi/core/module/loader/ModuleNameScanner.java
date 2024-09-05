package org.smoodi.core.module.loader;

import java.util.Set;

public interface ModuleNameScanner {

    Set<String> getModuleClassNames(String basePackage);
}
