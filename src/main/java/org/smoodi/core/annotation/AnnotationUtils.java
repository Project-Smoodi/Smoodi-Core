package org.smoodi.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotationUtils {

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T findAnnotation(
            final Class<?> klass,
            final Class<T> annotation
    ) {
        for (Annotation a : klass.getAnnotations()) {
            if (a.annotationType().equals(annotation)) {
                return (T) a;
            }
        }
        return null;
    }

    public static <T extends Annotation> T findAnnotation(
            final Object obj,
            final Class<T> annotation
    ) {
        return findAnnotation(obj.getClass(), annotation);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> List<T> findRepeatableAnnotation(
            final Class<?> klass,
            final Class<T> annotation
    ) {
        return (List<T>) Arrays.stream(klass.getAnnotations())
                .filter(a -> a.annotationType().equals(annotation)).toList();
    }

    public static <T extends Annotation> List<T> findRepeatableAnnotation(
            final Object obj,
            final Class<T> annotation
    ) {
        return findRepeatableAnnotation(obj.getClass(), annotation);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T findIncludeAnnotation(
            final Class<?> klass,
            final Class<T> annotation
    ) {
        final List<Annotation> forSearch = new ArrayList<>(Arrays.stream(klass.getAnnotations()).toList());
        final List<Annotation> newForSearch = new ArrayList<>();

        while (!forSearch.isEmpty()) {

            for (Annotation it : forSearch) {
                if (it.annotationType().equals(annotation)) {
                    return (T) it;
                }

                newForSearch.addAll(getAvailableAnnotations(it));
            }

            forSearch.clear();
            forSearch.addAll(newForSearch);
            newForSearch.clear();
        }

        return null;
    }

    public static <T extends Annotation> T findIncludeAnnotation(
            final Object obj,
            final Class<T> annotation
    ) {
        return findIncludeAnnotation(obj.getClass(), annotation);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> List<T> findIncludeRepeatableAnnotation(
            final Class<?> klass,
            final Class<T> annotation
    ) {
        final List<Annotation> forSearch = new ArrayList<>(Arrays.stream(klass.getAnnotations()).toList());
        final List<T> found = new ArrayList<>();
        final List<Annotation> newForSearch = new ArrayList<>();

        while (!forSearch.isEmpty()) {
            for (Annotation it : forSearch) {
                if (it.annotationType().equals(annotation)) {
                    found.add((T) it);
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

    public static <T extends Annotation> List<T> findIncludeRepeatableAnnotation(
            final Object obj,
            final Class<T> annotation
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
