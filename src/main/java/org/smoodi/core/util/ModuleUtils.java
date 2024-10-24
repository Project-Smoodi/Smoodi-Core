package org.smoodi.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.annotation.IoC;
import org.smoodi.core.annotation.ModuleInitConstructor;
import org.smoodi.core.module.ModuleDeclareError;
import org.smoodi.core.module.ModuleType;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModuleUtils {

    public static <T> Set<ModuleType<? extends T>> getModuleSubTypes(Class<T> klass) {
        return SubTypeUtils.getModuleSubTypes(klass);
    }

    public static <T> Constructor<T> findModuleInitConstructor(ModuleType<T> moduleType) {
        return ModuleInitConstructorSearcher.findModuleInitConstructor(moduleType);
    }

    public static void searchNonModuleDependency(Set<ModuleType<?>> moduleTypes) {
        NonModuleDependencySearch.search(moduleTypes);
    }

    public static void searchCircularDependency(Set<ModuleType<?>> moduleTypes) {
        CircularDependencySearch.search(moduleTypes);
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

        private static <T> Set<ModuleType<? extends T>> getModuleSubTypes(Class<T> klass) {

            final Set<Class<? extends T>> subTypes = new HashSet<>();

            for (Reflections reflection : reflections) {
                subTypes.addAll(reflection.getSubTypesOf(klass));
            }

            // Set to unmodifiable
            return subTypes.stream()
                    .map(ModuleType::of)
                    .collect(Collectors.toSet());
        }
    }

    private static final class ModuleInitConstructorSearcher {

        @SuppressWarnings("unchecked")
        private static <T> Constructor<T> findModuleInitConstructor(ModuleType<T> moduleType) {
            Constructor<T> emptyConstructor = null;

            Constructor<T>[] constructors =
                    (Constructor<T>[]) moduleType.getKlass().getConstructors();

            if (constructors.length == 1) {
                return constructors[0];
            }

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

    private static final class NonModuleDependencySearch {

        private static void search(Set<ModuleType<?>> moduleTypes) {
            for (ModuleType<?> moduleType : moduleTypes) {
                if (moduleType.getModuleInitConstructor() == null) {
                    continue;
                }

                for (Class<?> parameterType : moduleType.getModuleInitConstructor().getParameterTypes()) {
                    if (AnnotationUtils.findIncludeAnnotation(parameterType, IoC.class) == null) {
                        throw new ModuleDeclareError("Module cannot depend non-module type: dependency type \"" + parameterType.getName() + "\" of module type \"" + moduleType.getKlass().getName() + "\"");
                    }
                }
            }
        }
    }

    private static final class CircularDependencySearch {

        private static void search(Set<ModuleType<?>> moduleTypes) {
            final Map<Class<?>, Node> nodes = new HashMap<>();

            for (final ModuleType<?> moduleType : moduleTypes) {
                nodes.put(
                        moduleType.getKlass(),
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

        private static void dfsSearch(Map<Class<?>, Node> nodes, Node currentNode) {

            currentNode.visited = true;

            try {
                for (Class<?> parameterType : currentNode.moduleType.getModuleInitConstructor().getParameterTypes()) {
                    final var node = nodes.get(parameterType);
                    if (!node.searchFinished && node.visited) {
                        throw new CircularDependencyStackerException(node.moduleType.getKlass());
                    }
                    if (node.searchFinished && node.visited) {
                        continue;
                    }
                    dfsSearch(nodes, nodes.get(parameterType));
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
    }
}
