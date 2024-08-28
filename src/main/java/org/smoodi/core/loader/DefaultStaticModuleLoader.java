package org.smoodi.core.loader;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.container.ModuleContainer;

@Slf4j
public class DefaultStaticModuleLoader implements StaticModuleLoader {

    private final ModuleContainer moduleContainer = SmoodiFramework.getModuleContainer();

    private static final int STATIC_MODULE_COUNT = 2;

    @Override
    public int loadModules() {
        moduleContainer.save(moduleContainer);
        moduleContainer.save(SmoodiFramework.getInstance());

        log.info(LOG_PREFIX + "Statically-Initialized \"{}\" Modules are loaded.", STATIC_MODULE_COUNT);

        return STATIC_MODULE_COUNT;
    }
}
