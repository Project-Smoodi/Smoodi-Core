package org.smoodi.core.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smoodi.core.util.AnnotationUtils;

import java.lang.annotation.*;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
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
    @RepeatableTestAnnotation
    @RepeatableTestAnnotation(2)
    @RepeatableTestAnnotation(3)
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

    @RepeatAnnotatedTestAnnotation
    private static class IncludingRepeatAnnotatedClass {
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

    @Test
    public void 존재하는_반복된_어노테이션_탐색() {
        final List<RepeatableTestAnnotations> annotations =
                AnnotationUtils.findRepeatableAnnotation(RepeatAnnotatedClass.class, RepeatableTestAnnotations.class);

        Assertions.assertNotNull(annotations);
        var it = annotations.get(0).value();
        Assertions.assertEquals(1, annotations.getFirst().value()[0].value());
        Assertions.assertEquals(2, annotations.getFirst().value()[1].value());
        Assertions.assertEquals(3, annotations.getFirst().value()[2].value());
    }

    @Test
    public void 존재하는_내부에_포함된_반복된_어노테이션_탐색() {
        final List<RepeatableTestAnnotations> annotations =
                AnnotationUtils.findIncludeRepeatableAnnotation(IncludingRepeatAnnotatedClass.class, RepeatableTestAnnotations.class);

        Assertions.assertNotNull(annotations);
        Assertions.assertEquals(1, annotations.getFirst().value()[0].value());
        Assertions.assertEquals(2, annotations.getFirst().value()[1].value());
        Assertions.assertEquals(3, annotations.getFirst().value()[2].value());
    }
}
