package org.smoodi.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.annotation.Module;
import org.smoodi.core.annotation.ModuleInitConstructor;
import org.smoodi.core.module.ModuleDeclareError;
import org.smoodi.core.module.ModuleDependency;
import org.smoodi.core.module.ModuleType;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModuleUtils {

    /**
     * @see SubTypeUtils#getModuleSubTypes
     */
    public static <T> List<ModuleType<? extends T>> getModuleSubTypes(Class<T> klass) {
        return SubTypeUtils.getModuleSubTypes(klass);
    }

    /**
     * @see ModuleUtils.SubTypeUtils#findPrimaryModuleType
     */
    public static <T> ModuleType<? extends T> findPrimaryModuleType(ModuleType<T> moduleType) {
        return SubTypeUtils.findPrimaryModuleType(moduleType);
    }

    /**
     * @see ModuleInitConstructorSearcher#findModuleInitConstructor
     */
    public static <T> Constructor<T> findModuleInitConstructor(ModuleType<T> moduleType) {
        return ModuleInitConstructorSearcher.findModuleInitConstructor(moduleType);
    }

    /**
     * @see CircularDependencySearch#search
     */
    public static void searchCircularDependency(List<ModuleType<?>> moduleTypes) {
        CircularDependencySearch.search(moduleTypes);
    }

    public static <T> Comparator<T> comparator() {
        return new CircularDependencySearch.ComparatorModule<T>();
    }

    private static final class SubTypeUtils {
        private static final List<Reflections> reflections;

        static {
            if (SmoodiFramework.getMainClass().getPackage().getName().startsWith(
                    SmoodiFramework.SMOODI_PACKAGE_PREFIX)) {
                reflections = List.of(
                        new Reflections(SmoodiFramework.SMOODI_PACKAGE_PREFIX)
                );
            } else {
                reflections = List.of(
                        new Reflections(SmoodiFramework.getMainClass().getPackage().getName()),
                        new Reflections(SmoodiFramework.SMOODI_PACKAGE_PREFIX)
                );
            }
        }

        private static <T> List<ModuleType<? extends T>> getModuleSubTypes(Class<T> klass) {

            final List<Class<? extends T>> subTypes = new UtilCollection.SortedList<>(comparator());

            for (Reflections reflection : reflections) {
                subTypes.addAll(reflection.getSubTypesOf(klass));
            }

            // Set to unmodifiable
            return subTypes.stream()
                    .filter(ModuleType::isKlassModule)
                    .map(ModuleType::of)
                    .collect(Collectors.toList());
        }

        private static <T> ModuleType<? extends T> findPrimaryModuleType(ModuleType<T> moduleType) {
            final var instantiableSubTypes = moduleType.getSubTypes().stream()
                    .filter(it -> it.isInstantiableKlass() && AnnotationUtils.findIncludeAnnotation(it.getKlass(), Module.class) != null)
                    .collect(Collectors.toList());
            if (moduleType.isInstantiableKlass()) instantiableSubTypes.add(moduleType);

            if (instantiableSubTypes.isEmpty()) return null;
            if (instantiableSubTypes.size() == 1) {
                return instantiableSubTypes.getFirst();
            }

            var primary = instantiableSubTypes.stream().filter(
                    it -> Objects.requireNonNull(
                            AnnotationUtils.findIncludeAnnotation(it.getKlass(), Module.class)
                    ).isPrimary()
            ).toList();

            if (primary.size() > 1) {
                throw new ModuleDeclareError("Many primary module found. Primary module MUST BE one: " + moduleType.getKlass().getName());
            } else if (primary.isEmpty()) {
                throw new ModuleDeclareError("Many modules found BUT the primary module does not exist: " + moduleType.getKlass().getName());
            }

            return primary.getFirst();
        }
    }

    private static final class ModuleInitConstructorSearcher {

        @SuppressWarnings("unchecked")
        private static <T> Constructor<T> findModuleInitConstructor(ModuleType<T> moduleType) {
            if (!moduleType.isInstantiableKlass()) {
                throw new IllegalArgumentException("Module type is not instantiable, cannot have module init constructor.");
            }

            Constructor<T> emptyConstructor = null;
            Constructor<T>[] constructors =
                    (Constructor<T>[]) moduleType.getKlass().getConstructors();

            if (constructors.length == 1) {
                return constructors[0];
            }

            // TODO("ModuleInitConstructor가 여러 개 할당된 경우 에러 표시")
            for (Constructor<T> constructor : constructors) {
                if (AnnotationUtils.findIncludeAnnotation(constructor, ModuleInitConstructor.class) != null) {
                    return constructor;
                }

                if (constructor.getParameterCount() == 0) {
                    emptyConstructor = constructor;
                }
            }

            if (emptyConstructor != null) {
                return emptyConstructor;
            }

            throw new ModuleDeclareError(
                    "Cannot found module initialization constructor in class: "
                            + moduleType.getKlass().getName()
                            + " with searcher: "
                            + ModuleInitConstructorSearcher.class.getName()
            );
        }
    }

    private static final class CircularDependencySearch {

        private static void search(List<ModuleType<?>> moduleTypes) {
            final Map<ModuleType<?>, Node> nodes = new HashMap<>();

            for (final ModuleType<?> moduleType : moduleTypes) {
                nodes.put(
                        moduleType,
                        Node.of(moduleType));
            }

            try {
                while (true) {
                    final var willRoot = nodes.values().stream().filter(it -> !it.visited).findFirst();
                    if (willRoot.isEmpty()) {
                        return;
                    }

                    dfsSearch(nodes, willRoot.get());
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        private static void dfsSearch(Map<ModuleType<?>, Node> nodes, Node currentNode) {

            currentNode.visited = true;

            try {
                for (ModuleDependency<?, ?> parameterType : currentNode.moduleType.getModuleInitConstructor().getDependencies()) {
                    final var node = nodes.get(parameterType.getModuleTypeForInjection());
                    if (!node.searchFinished && node.visited) {
                        throw new CircularDependencyStackerException(node.moduleType.getKlass());
                    }
                    if (node.searchFinished && node.visited) {
                        continue;
                    }
                    dfsSearch(nodes, nodes.get(parameterType.getModuleTypeForInjection()));
                }
            } catch (CircularDependencyStackerException e) {
                if (e.circularDependencyRoot == currentNode.moduleType.getKlass()) {
                    throw e.toIllegalArgumentException();
                }
                throw new CircularDependencyStackerException(currentNode.moduleType.getKlass(), e);
            }

            currentNode.searchFinished = true;
        }

        @AllArgsConstructor
        private static class Node {
            ModuleType<?> moduleType;
            boolean visited;
            boolean searchFinished;

            public static Node of(ModuleType<?> moduleType) {
                return new Node(
                        moduleType, false, false
                );
            }
        }

        private static class CircularDependencyStackerException extends RuntimeException {
            private final Class<?> circularDependencyRoot;

            private final List<Class<?>> stack;

            private IllegalArgumentException toIllegalArgumentException() {
                final var builder = new StringBuilder();

                for (Class<?> klass : this.stack) {
                    builder.append(klass).append(" - ");
                }
                builder.append(circularDependencyRoot);

                return new IllegalArgumentException(
                        "Circular Dependency found: " + builder
                );
            }

            private CircularDependencyStackerException(Class<?> klass, CircularDependencyStackerException cause) {
                this.circularDependencyRoot = cause.circularDependencyRoot;
                cause.stack.add(klass);
                this.stack = cause.stack;

            }

            private CircularDependencyStackerException(Class<?> circularDependencyRoot) {
                this.circularDependencyRoot = circularDependencyRoot;
                this.stack = new ArrayList<>();
                this.stack.add(circularDependencyRoot);
            }

            @Override
            public synchronized Throwable fillInStackTrace() {
                return null;
            }
        }

        private static class ComparatorModule<T> implements Comparator<T> {

            @Override
            public int compare(final T o1, final T o2) {

                assert o1 != null;
                assert o2 != null;

                var o1Anno = AnnotationUtils.findIncludeAnnotation(o1, Module.class);
                var o2Anno = AnnotationUtils.findIncludeAnnotation(o2, Module.class);

                assert o1Anno != null;
                assert o2Anno != null;

                return Byte.compare(o1Anno.order(), o2Anno.order());
            }
        }
    }
}
