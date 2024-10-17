package org.smoodi.core.module.loader;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.module.container.ModuleContainer;

@Slf4j
public class DefaultStaticModuleLoader implements ModuleLoader {

    private static final int STATIC_MODULE_COUNT = 2;

    @Override
    public int loadModules() {
        final ModuleContainer moduleContainer = SmoodiFramework.getInstance().getModuleContainer();

        moduleContainer.save(moduleContainer);
        moduleContainer.save(SmoodiFramework.getInstance());

        log.info(LOG_PREFIX + "Statically-Initialized \"{}\" Modules are loaded.", STATIC_MODULE_COUNT);

        return STATIC_MODULE_COUNT;
    }
}
