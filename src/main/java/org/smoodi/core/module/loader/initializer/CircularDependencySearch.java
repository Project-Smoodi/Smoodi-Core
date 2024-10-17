package org.smoodi.core.module.loader.initializer;

import lombok.AllArgsConstructor;
import org.smoodi.core.module.ModuleType;

import java.util.*;

public final class CircularDependencySearch {

    public static void search(Set<ModuleType<?>> moduleTypes) {
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

        public IllegalArgumentException toIllegalArgumentException() {
            final var builder = new StringBuilder();

            for (Class<?> klass : this.stack) {
                builder.append(klass).append(" - ");
            }
            builder.append(circularDependencyRoot);

            return new IllegalArgumentException(
                    "Circular Dependency found: " + builder
            );
        }

        public CircularDependencyStackerException(Class<?> klass, CircularDependencyStackerException cause) {
            this.circularDependencyRoot = cause.circularDependencyRoot;
            cause.stack.add(klass);
            this.stack = cause.stack;

        }

        public CircularDependencyStackerException(Class<?> circularDependencyRoot) {
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
