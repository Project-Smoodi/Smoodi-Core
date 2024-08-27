package org.smoodi.core.loader;

import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.container.ModuleContainer;

public class DefaultStaticModuleLoader implements StaticModuleLoader {

    private final ModuleContainer moduleContainer = SmoodiFramework.getModuleContainer();

    @Override
    public void loadModules() {
        moduleContainer.save(moduleContainer);
        moduleContainer.save(SmoodiFramework.getInstance());
    }
}
