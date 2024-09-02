package org.smoodi.core.module.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SubprojectPackageManager;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class SmoodiProjectModuleLoader implements ModuleLoader {

    private final BasePackageModuleLoader packageLoader;

    @Override
    public int loadModules() {
        final Map<String, Package> projects = Map.copyOf(SubprojectPackageManager.getSubprojects());

        AtomicInteger totalModuleCount = new AtomicInteger();

        projects.forEach((key, value) -> {
                    final int moduleCount = packageLoader.loadModules(value.getName());

                    log.info(LOG_PREFIX + "Smoodi project \"{}\" of pacakge \"{}\" \"{}\" modules are loaded.", key, value.getName(), moduleCount);

                    totalModuleCount.addAndGet(moduleCount);
                }
        );

        log.info(LOG_PREFIX + "Smoodi project modules total {} loaded", totalModuleCount.get());

        return totalModuleCount.get();
    }
}
