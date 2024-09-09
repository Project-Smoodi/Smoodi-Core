package org.smoodi.core.module.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SubprojectPackageManager;
import org.smoodi.core.module.loader.initializer.ModuleInitializer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class SmoodiProjectModuleLoader implements ModuleLoader {

    private final ModuleClassScanner moduleClassScanner;

    private final ModuleInitializer moduleInitializer;

    @Override
    public int loadModules() {
        final Map<String, Package> projects = Map.copyOf(SubprojectPackageManager.getSubprojects());

        AtomicInteger totalModuleCount = new AtomicInteger();

        projects.forEach((key, value) -> {
                    final Set<Class<?>> moduleClasses = moduleClassScanner.getModuleClasses(value.getName());

                    moduleInitializer.initialize(moduleClasses.stream().toList());

                    log.info(LOG_PREFIX + "Smoodi project \"{}\" of pacakge \"{}\" \"{}\" modules are loaded.", key, value.getName(), moduleClasses.size());

                    totalModuleCount.addAndGet(moduleClasses.size());
                }
        );

        log.info(LOG_PREFIX + "Smoodi project modules total {} loaded", totalModuleCount.get());

        return totalModuleCount.get();
    }
}
