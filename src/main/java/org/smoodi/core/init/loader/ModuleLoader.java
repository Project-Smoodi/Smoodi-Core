package org.smoodi.core.init.loader;

public interface ModuleLoader {

    void loadModules(String basePackage);

    static ModuleLoader getInstance() {
        return BasePackageModuleLoader.getInstance();
    }
}
