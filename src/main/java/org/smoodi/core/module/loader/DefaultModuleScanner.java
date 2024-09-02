package org.smoodi.core.module.loader;

import javassist.bytecode.ClassFile;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.JavassistHelper;
import org.smoodi.core.module.Module;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DefaultModuleScanner implements ModuleScanner {

    // Reflections Scanner
    private final Scanner rs = new AnnotatedClassScanner(Module.class);

    // Class Loader
    private final ClassLoader cl = ClassLoader.getSystemClassLoader();

    @Override
    public Set<String> getModuleClassNames(String basePackage) {

        final Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .addClassLoaders(cl)
                        .setUrls(ClasspathHelper.forPackage(basePackage))
                        .forPackages(basePackage)
                        .setScanners(rs)
        );

        final Set<String> moduleNames = reflections.getAll(rs);
        moduleNames.remove(Module.class.getName());

        log.info("Scan {} modules with basePackage: \"{}\"", moduleNames.size(), basePackage);

        return moduleNames;
    }

    private record AnnotatedClassScanner(
            Class<? extends Annotation> annotation
    ) implements Scanner {

        @Override
        public List<Map.Entry<String, String>> scan(ClassFile classFile) {
            final List<Map.Entry<String, String>> entries = new ArrayList<>();

            if (!isClass(classFile)) {
                return entries;
            }

            JavassistHelper.getAnnotations(classFile::getAttribute).forEach((anno) -> {
                if (anno.equals(this.annotation.getName())) {
                    entries.add(
                            entry(classFile.getName(), anno)
                    );
                }
            });

            return entries;
        }

        private boolean isClass(ClassFile classFile) {
            return !classFile.isInterface() && !classFile.isAbstract();
        }
    }
}
