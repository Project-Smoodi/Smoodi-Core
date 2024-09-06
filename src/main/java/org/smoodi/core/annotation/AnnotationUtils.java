package org.smoodi.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotationUtils {

    public static Annotation findAnnotation(
            final Class<?> klass,
            final Class<? extends Annotation> annotation
    ) {
        return Arrays.stream(klass.getAnnotations())
                .filter(a -> a.annotationType().equals(annotation))
                .findFirst()
                .orElse(null);
    }

    public static Annotation findAnnotation(
            final Object obj,
            final Class<? extends Annotation> annotation
    ) {
        return findAnnotation(obj.getClass(), annotation);
    }

    public static List<Annotation> findRepeatableAnnotation(
            final Class<?> klass,
            final Class<? extends Annotation> annotation
    ) {
        return Arrays.stream(klass.getAnnotations()).filter(a -> a.annotationType().equals(annotation)).toList();
    }

    public static List<Annotation> findRepeatableAnnotation(
            final Object obj,
            final Class<? extends Annotation> annotation
    ) {
        return findRepeatableAnnotation(obj.getClass(), annotation);
    }

    public static Annotation findIncludeAnnotation(
            final Class<?> klass,
            final Class<? extends Annotation> annotation
    ) {
        final List<Annotation> forSearch = new ArrayList<>(Arrays.stream(klass.getAnnotations()).toList());
        final List<Annotation> newForSearch = new ArrayList<>();

        while (!forSearch.isEmpty()) {

            for (Annotation it : forSearch) {
                if (it.annotationType().equals(annotation)) {
                    return it;
                }

                newForSearch.addAll(getAvailableAnnotations(it));
            }

            forSearch.clear();
            forSearch.addAll(newForSearch);
            newForSearch.clear();
        }

        return null;
    }

    public static Annotation findIncludeAnnotation(
            final Object obj,
            final Class<? extends Annotation> annotation
    ) {
        return findIncludeAnnotation(obj.getClass(), annotation);
    }

    public static List<Annotation> findIncludeRepeatableAnnotation(
            final Class<?> klass,
            final Class<? extends Annotation> annotation
    ) {
        //
        final List<Annotation> forSearch = new ArrayList<>(Arrays.stream(klass.getAnnotations()).toList());
        final List<Annotation> found = new ArrayList<>();
        final List<Annotation> newForSearch = new ArrayList<>();

        while (!forSearch.isEmpty()) {
            for (Annotation it : forSearch) {
                if (it.annotationType().equals(annotation)) {
                    found.add(it);
                } else {
                    newForSearch.addAll(getAvailableAnnotations(it));
                }
            }

            forSearch.clear();
            forSearch.addAll(newForSearch);
            newForSearch.clear();
        }

        if (found.isEmpty()) {
            return null;
        }
        return found;
    }

    public static List<Annotation> findIncludeRepeatableAnnotation(
            final Object obj,
            final Class<? extends Annotation> annotation
    ) {
        return findIncludeRepeatableAnnotation(obj.getClass(), annotation);
    }

    private static List<Annotation> getAvailableAnnotations(final Class<?> klass) {
        return Arrays.stream(klass.getAnnotations())
                .filter(it -> !it.annotationType().equals(Target.class) && !it.annotationType().equals(Retention.class))
                .toList();
    }

    private static List<Annotation> getAvailableAnnotations(final Object obj) {
        if (obj instanceof Annotation) {
            return getAvailableAnnotations(((Annotation) obj).annotationType());
        } else {
            return getAvailableAnnotations(obj.getClass());
        }
    }
}
