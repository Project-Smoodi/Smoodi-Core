package org.smoodi.core.module.loader;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.smoodi.core.module.Module;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class DefaultModuleClassScanner implements ModuleClassScanner {

    @Override
    public Set<Class<?>> getModuleClasses(String basePackage) {

        final Reflections reflections = new Reflections(basePackage);

        final Set<Class<?>> moduleClasses = reflections.getTypesAnnotatedWith(Module.class)
                .stream().filter(it -> !it.isAnnotation())
                .collect(Collectors.toSet());

        log.info("Scan {} modules with basePackage: \"{}\"", moduleClasses.size(), basePackage);

        return moduleClasses;
    }
}
