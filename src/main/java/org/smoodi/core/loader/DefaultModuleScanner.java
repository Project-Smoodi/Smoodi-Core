package org.smoodi.core.loader;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.smoodi.core.Module;

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
}
