package org.smoodi.core.loader;

public interface PackageBasedModuleLoader extends ModuleLoader {

    void loadModules(String basePackage);
}
