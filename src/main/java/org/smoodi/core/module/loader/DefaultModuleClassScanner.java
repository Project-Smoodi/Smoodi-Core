package org.smoodi.core.module.loader;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.smoodi.core.module.Configuration;
import org.smoodi.core.module.Module;
import org.smoodi.core.module.SmoodiDefaultConfiguration;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

@Slf4j
public class DefaultModuleClassScanner implements ModuleClassScanner {

    private static final List<AnnotatedElement> annotations = new ArrayList<>(
            List.of(Module.class, Configuration.class, SmoodiDefaultConfiguration.class));

    public void addModuleAnnotation(Class<? extends Annotation> annotation) {
        annotations.add(annotation);
    }

    @SafeVarargs
    public final void addModuleAnnotation(Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            addModuleAnnotation(annotation);
        }
    }

    private final org.reflections.util.QueryFunction<org.reflections.Store, Class<?>> reflectionsQuery =
            SubTypes.of(TypesAnnotated.with(
                    new HashSet<>(annotations)
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
