package org.smoodi.core.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.SmoodiProjects;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class SmoodiProjectModuleLoader implements StaticModuleLoader {

    private final BasePackageModuleLoader packageLoader;

    @Override
    public void loadModules() {
        final Set<SmoodiProjects> projects = SmoodiFramework.getAddedSmoodiProjects();

        projects.forEach(it -> {
                    packageLoader.loadModules(it.basePackage);

                    log.info(LOG_PREFIX + "Smoodi project \"{}\" of pacakge \"{}\" modules are loaded.", it, it.basePackage);
                }
        );
    }
}
