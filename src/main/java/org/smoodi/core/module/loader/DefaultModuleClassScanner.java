package org.smoodi.core.module.loader;

import javassist.bytecode.ClassFile;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.smoodi.core.annotation.AnnotationUtils;
import org.smoodi.core.annotation.Module;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DefaultModuleClassScanner implements ModuleClassScanner {

    private static final Class<? extends Annotation> MODULE_ANNOTATION =
            Module.class;

    private final Scanner classScanner = new AnnotatedClassScanner();

    @SneakyThrows(ClassNotFoundException.class)
    @Override
    public Set<Class<?>> getModuleClasses(String basePackage) {
        final Set<Class<?>> moduleClasses = new HashSet<>();

        for (String s : new Reflections(basePackage, classScanner).getAll(classScanner)) {

            Class<?> it = Class.forName(s);

            if (!it.isAnnotation()) {
                moduleClasses.add(it);
            }
        }

        log.info("Scan {} modules with basePackage: \"{}\"", moduleClasses.size(), basePackage);

        return moduleClasses;
    }

    private static final class AnnotatedClassScanner implements Scanner {

        @SneakyThrows(ClassNotFoundException.class)
        @Override
        public List<Map.Entry<String, String>> scan(ClassFile classFile) {

            final var annotation = AnnotationUtils.findIncludeAnnotation(
                    Class.forName(classFile.getName()),
                    MODULE_ANNOTATION
            );

            if (annotation != null) {
                return List.of(
                        Map.entry(
                                classFile.getName(),
                                annotation.annotationType().getName()
                        )
                );
            }

            return List.of();
        }
    }
}
