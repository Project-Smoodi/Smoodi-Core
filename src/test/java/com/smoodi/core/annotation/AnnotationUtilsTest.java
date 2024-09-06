package com.smoodi.core.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smoodi.core.annotation.AnnotationUtils;

import java.lang.annotation.*;
import java.util.List;

public class AnnotationUtilsTest {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestAnnotation {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestAnnotation2 {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @TestAnnotation
    private @interface IncludingTestAnnotation {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(value = RepeatableTestAnnotations.class)
    private @interface RepeatableTestAnnotation {

        int value() default 1;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface RepeatableTestAnnotations {

        RepeatableTestAnnotation[] value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface RepeatAnnotatedTestAnnotation {
    }

    private static class JustClass {
    }

    @TestAnnotation
    private static class AnnotatedClass {
    }

    @IncludingTestAnnotation
    private static class IncludingAnnotatedClass {
    }

    @RepeatableTestAnnotation
    @RepeatableTestAnnotation(2)
    @RepeatableTestAnnotation(3)
    private static class RepeatAnnotatedClass {
    }

    @Test
    public void 존재하는_단일_어노테이션_탐색() {
        Assertions.assertNotNull(
                AnnotationUtils.findAnnotation(AnnotatedClass.class, TestAnnotation.class)
        );
    }

    @Test
    public void 존재하지_않는_단일_어노테이션_탐색() {
        Assertions.assertNull(
                AnnotationUtils.findAnnotation(JustClass.class, TestAnnotation.class)
        );
    }

    @Test
    public void 내부에_포함된_단일_어노테이션_탐색() {
        Assertions.assertNotNull(
                AnnotationUtils.findIncludeAnnotation(IncludingAnnotatedClass.class, TestAnnotation.class)
        );
    }

    @Test
    public void 내부에_포함되지_않은_단일_어노테이션_탐색() {
        Assertions.assertNull(
                AnnotationUtils.findIncludeAnnotation(IncludingAnnotatedClass.class, TestAnnotation2.class)
        );
    }

}
