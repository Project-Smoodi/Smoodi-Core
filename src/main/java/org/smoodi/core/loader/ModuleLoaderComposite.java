package org.smoodi.core.loader;

import org.smoodi.core.SmoodiFramework;

public class ModuleLoaderComposite implements PackageBasedModuleLoader {

    private final PackageBasedModuleLoader packageBasedModuleLoader =
            new BasePackageModuleLoader();

    private final StaticModuleLoader smoodiProjectModuleLoader =
            new SmoodiProjectModuleLoader((BasePackageModuleLoader) packageBasedModuleLoader);

    private final StaticModuleLoader staticModuleLoader =
            new DefaultStaticModuleLoader();

    @Override
    public void loadModules(String basePackage) {
        staticModuleLoader.loadModules();
        packageBasedModuleLoader.loadModules(
                SmoodiFramework.class.getPackage().getName());
        smoodiProjectModuleLoader.loadModules();
        packageBasedModuleLoader.loadModules(SmoodiFramework.getMainClass().getPackage().getName());
    }
}
