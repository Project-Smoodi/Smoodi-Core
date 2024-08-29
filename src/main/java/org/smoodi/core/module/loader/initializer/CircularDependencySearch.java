package org.smoodi.core.module.loader.initializer;

import lombok.AllArgsConstructor;

import java.lang.reflect.Constructor;
import java.util.*;

public final class CircularDependencySearch {

    public static void search(List<Constructor<Object>> constructors) {
        final Map<Class<Object>, Node<Object>> nodes = new HashMap<>();

        for (final Constructor<Object> constructor : constructors) {
            nodes.put(
                    constructor.getDeclaringClass(),
                    Node.of(constructor));
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

    private static void dfsSearch(Map<Class<Object>, Node<Object>> nodes, Node<Object> currentNode) {

        currentNode.visited = true;

        try {
            for (Class<?> parameterType : currentNode.constructor.getParameterTypes()) {
                final var node = nodes.get(parameterType);
                if (!node.searchFinished && node.visited) {
                    throw new CircularDependencyStackerException(node.klass);
                }
                dfsSearch(nodes, nodes.get(parameterType));
            }
        } catch (CircularDependencyStackerException e) {
            if (e.circularDependencyRoot == currentNode.klass) {
                throw e.toIllegalArgumentException();
            }
            throw new CircularDependencyStackerException(currentNode.klass, e);
        }

        currentNode.searchFinished = true;
    }

    @AllArgsConstructor
    private static class Node<T> {
        Class<T> klass;
        Constructor<T> constructor;
        List<Class<?>> parameterTypes;
        boolean visited;
        boolean searchFinished;

        public static <T> Node<T> of(Constructor<T> constructor) {
            return new Node<>(
                    constructor.getDeclaringClass(),
                    constructor,
                    Arrays.stream(constructor.getParameterTypes()).toList(),
                    false, false
            );
        }
    }

    private static class CircularDependencyStackerException extends RuntimeException {
        private final Class<Object> circularDependencyRoot;

        private final List<Class<Object>> stack;

        public IllegalArgumentException toIllegalArgumentException() {
            final var builder = new StringBuilder();

            for (Class<Object> klass : this.stack) {
                builder.append(klass).append(" - ");
            }
            builder.append(circularDependencyRoot);

            return new IllegalArgumentException(
                    "Circular Dependency found: " + builder
            );
        }

        public CircularDependencyStackerException(Class<Object> klass, CircularDependencyStackerException cause) {
            this.circularDependencyRoot = cause.circularDependencyRoot;
            cause.stack.add(klass);
            this.stack = cause.stack;

        }

        public CircularDependencyStackerException(Class<Object> circularDependencyRoot) {
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
