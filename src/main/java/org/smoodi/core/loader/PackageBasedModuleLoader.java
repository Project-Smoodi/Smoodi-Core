package org.smoodi.core.loader;

public interface PackageBasedModuleLoader extends ModuleLoader {

    int loadModules(String basePackage);
}
