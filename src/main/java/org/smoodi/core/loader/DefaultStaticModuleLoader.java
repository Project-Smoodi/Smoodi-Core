package org.smoodi.core.loader;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.container.ModuleContainer;

@Slf4j
public class DefaultStaticModuleLoader implements StaticModuleLoader {

    private final ModuleContainer moduleContainer = SmoodiFramework.getModuleContainer();

    @Override
    public void loadModules() {
        moduleContainer.save(moduleContainer);
        moduleContainer.save(SmoodiFramework.getInstance());

        log.info(LOG_PREFIX + "Statically-Initialized \"2\" Modules are loaded.");
    }
}
