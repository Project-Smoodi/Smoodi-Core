package org.smoodi.core.module.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.SmoodiProjects;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class SmoodiProjectModuleLoader implements StaticModuleLoader {

    private final BasePackageModuleLoader packageLoader;

    @Override
    public int loadModules() {
        final Set<SmoodiProjects> projects = SmoodiFramework.getAddedSmoodiProjects();

        AtomicInteger totalModuleCount = new AtomicInteger();

        projects.forEach(it -> {
                    final int moduleCount = packageLoader.loadModules(it.basePackage);

                    log.info(LOG_PREFIX + "Smoodi project \"{}\" of pacakge \"{}\" \"{}\" modules are loaded.", it, it.basePackage, moduleCount);

                    totalModuleCount.addAndGet(moduleCount);
                }
        );

        log.info(LOG_PREFIX + "Smoodi project modules total {} loaded", totalModuleCount.get());

        return totalModuleCount.get();
    }
}
