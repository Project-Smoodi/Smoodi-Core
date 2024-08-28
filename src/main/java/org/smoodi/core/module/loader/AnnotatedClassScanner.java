package org.smoodi.core.module.loader;

import javassist.bytecode.ClassFile;
import lombok.RequiredArgsConstructor;
import org.reflections.scanners.Scanner;
import org.reflections.util.JavassistHelper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AnnotatedClassScanner implements Scanner {

    private final Class<? extends Annotation> annotation;

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
