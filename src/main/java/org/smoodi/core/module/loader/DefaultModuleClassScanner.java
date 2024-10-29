package org.smoodi.core.module.loader;

import javassist.bytecode.ClassFile;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.smoodi.core.annotation.Module;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.AnnotationUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DefaultModuleClassScanner implements ModuleClassScanner {

    private static final Class<Module> MODULE_ANNOTATION =
            Module.class;

    private final AnnotatedClassScanner classScanner = new AnnotatedClassScanner();

    @SneakyThrows(ClassNotFoundException.class)
    @Override
    public Set<ModuleType<?>> getModuleClasses(String basePackage) {
        final Set<ModuleType<?>> moduleClasses = new HashSet<>();

        for (String s : new Reflections(basePackage, classScanner.setPackagePrefix(basePackage)).getAll(classScanner)) {

            Class<?> it = Class.forName(s);

            if (!it.isAnnotation()) {
                moduleClasses.add(ModuleType.of(it));
            }
        }

        log.info("Scan {} modules with basePackage: \"{}\"", moduleClasses.size(), basePackage);

        return moduleClasses;
    }

    private static final class AnnotatedClassScanner implements Scanner {

        private String packagePrefix = "";

        public AnnotatedClassScanner setPackagePrefix(String packagePrefix) {
            this.packagePrefix = packagePrefix;
            return this;
        }

        @SneakyThrows(ClassNotFoundException.class)
        @Override
        public List<Map.Entry<String, String>> scan(ClassFile classFile) {

            final var klass = Class.forName(classFile.getName());

            if (!klass.getPackageName().startsWith(packagePrefix)) {
                return List.of();
            }

            final var annotation = AnnotationUtils.findIncludeAnnotation(
                    klass,
                    MODULE_ANNOTATION
            );

            if (annotation != null && annotation.IoC()) {
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
