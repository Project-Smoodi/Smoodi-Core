package org.smoodi.core.loader;

import java.util.Set;

public interface ModuleScanner {

    Set<String> getModuleClassNames(String basePackage);

    static ModuleScanner getDefaultInstance() {
        return DefaultModuleScanner.getInstance();
    }
}
