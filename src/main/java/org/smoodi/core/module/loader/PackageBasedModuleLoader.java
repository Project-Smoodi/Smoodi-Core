package org.smoodi.core.module.loader;

public interface PackageBasedModuleLoader {

    String LOG_PREFIX = "Module Loader :: ";

    int loadModules(String basePackage);
}
