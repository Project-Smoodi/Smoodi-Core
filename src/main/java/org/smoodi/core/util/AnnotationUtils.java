package org.smoodi.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.annotation.array.UnmodifiableArray;

import javax.annotation.Nullable;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotationUtils {

    private static final List<Class<? extends Annotation>> METADATA_ANNOTATIONS = List.of(
            Target.class,
            Retention.class,
            Documented.class,
            Inherited.class,
            Repeatable.class,
            Deprecated.class
    );

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
        final List<Annotation> forSearch = new ArrayList<>(getAvailableAnnotations(klass));
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

    private static List<Annotation> getAvailableAnnotations(final Object obj) {
        return filterAvailableAnnotations(
                AnnotationExtractor.getAnnotations(obj)
        );
    }

    private static List<Annotation> filterAvailableAnnotations(final Annotation[] annotations) {
        return Arrays.stream(annotations)
                .filter(it -> !METADATA_ANNOTATIONS.contains(it.annotationType()))
                .toList();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class AnnotationExtractor {

        private static final Set<String> ANNOTATION_GETTER_METHOD_NAMES = Set.of(
                "getAnnotations",
                "annotations",
                "getAnnotation",
                "annotation"
        );

        @EmptyableArray
        @UnmodifiableArray
        @NotNull
        public static Annotation[] getAnnotations(final Object o) {

            Annotation[] result = getAnnotationsByTypeCasting(o);

            if (result == null) {
                result = getAnnotationsOrNullByNamedMethod(o);
            }
            if (result == null) {
                result = getAnnotationsByGetClassMethod(o);
            }
            if (result == null) {
                return new Annotation[0];
            }

            return result;
        }

        @Nullable
        public static Annotation[] getAnnotationsByTypeCasting(final Object o) {
            if (o instanceof Class<?>) {
                return ((Class<?>) o).getAnnotations();
            } else if (o instanceof Annotation) {
                return ((Annotation) o).annotationType().getAnnotations();
            } else {
                return null;
            }
        }

        @SneakyThrows({IllegalArgumentException.class, InvocationTargetException.class, IllegalAccessException.class})
        @Nullable
        private static Annotation[] getAnnotationsOrNullByNamedMethod(final Object o) {
            var methods = Arrays.stream(o.getClass().getMethods()).filter(it ->
                    ANNOTATION_GETTER_METHOD_NAMES.contains(it.getName())
                            && it.getParameters().length == 0
                            && it.canAccess(o)
            ).toList();

            if (methods.isEmpty()) {
                return null;
            }

            for (Method method : methods) {
                final Object returnValue = method.invoke(o);

                if (returnValue instanceof Collection
                        && !((Collection<?>) returnValue).isEmpty()) {
                    return (Annotation[]) ((Collection<?>) returnValue).toArray();
                }

                if (returnValue instanceof Annotation[]
                        && !Arrays.stream(((Annotation[]) returnValue)).toList().isEmpty()) {
                    return (Annotation[]) returnValue;
                }
            }

            return null;
        }

        @Nullable
        private static Annotation[] getAnnotationsByGetClassMethod(final Object o) {
            var result = o.getClass().getAnnotations();

            if (result.length == 0) {
                return null;
            }
            return result;
        }
    }
}
