package org.smoodi.core.module.loader;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.smoodi.core.module.Configuration;
import org.smoodi.core.module.Module;
import org.smoodi.core.module.SmoodiDefaultConfiguration;

import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

@Slf4j
public class DefaultModuleClassScanner implements ModuleClassScanner {

    private final org.reflections.util.QueryFunction<org.reflections.Store, Class<?>> reflectionsQuery =
            SubTypes.of(TypesAnnotated.with(
                    Module.class, Configuration.class, SmoodiDefaultConfiguration.class
            )).asClass(ClassLoader.getSystemClassLoader());

    @Override
    public Set<Class<?>> getModuleClasses(String basePackage) {

        final Set<Class<?>> moduleClasses = new Reflections(basePackage)
                .get(reflectionsQuery)
                .stream().filter(it -> !it.isAnnotation())
                .collect(Collectors.toSet());

        log.info("Scan {} modules with basePackage: \"{}\"", moduleClasses.size(), basePackage);

        return moduleClasses;
    }
}
