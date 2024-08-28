package org.smoodi.core.module.loader;

public interface PackageBasedModuleLoader extends ModuleLoader {

    int loadModules(String basePackage);
}
