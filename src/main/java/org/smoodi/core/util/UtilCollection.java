package org.smoodi.core.util;

import java.util.*;

public final class UtilCollection {

    public final static class SortedList<E> extends AbstractList<E> {

        private final ArrayList<E> inner;

        private final Comparator<? super E> comparator;

        public static <E> SortedList<E> ofModule(Collection<E> collection) {
            return new SortedList<>(collection, ModuleUtils.comparator());
        }

        public static <E> SortedList<E> ofModule() {
            return new SortedList<>(ModuleUtils.comparator());
        }

        public SortedList(Comparator<? super E> comparator) {
            this.inner = new ArrayList<>();
            this.comparator = comparator;
        }

        public SortedList(Collection<E> inner, Comparator<? super E> comparator) {
            this.inner = new ArrayList<>(inner);
            this.comparator = comparator;
        }

        @Override
        public E get(int index) {
            return inner.get(index);
        }

        @Override
        public int size() {
            return inner.size();
        }

        @Override
        public boolean add(E e) {
            var result = inner.add(e);
            inner.sort(comparator);
            return result;
        }

        @Override
        public boolean addAll(java.util.Collection<? extends E> c) {
            var result = inner.addAll(c);
            inner.sort(comparator);
            return result;
        }

        @Override
        public boolean addAll(int index, java.util.Collection<? extends E> c) {
            throw new UnsupportedOperationException("Indexed addAll is not supported");
        }

        @Override
        public E set(int index, E element) {
            throw new UnsupportedOperationException("Indexed set is not supported");
        }

        @Override
        public void add(int index, E element) {
            throw new UnsupportedOperationException("Indexed add is not supported");
        }

        @Override
        public E remove(int index) {
            return inner.remove(index);
        }

        @Override
        public boolean remove(Object o) {
            return inner.remove(o);
        }

        @Override
        public boolean removeAll(java.util.Collection<?> c) {
            return inner.removeAll(c);
        }

        @Override
        public boolean retainAll(java.util.Collection<?> c) {
            return inner.retainAll(c);
        }

        @Override
        public void clear() {
            inner.clear();
        }

        @Override
        public boolean contains(Object o) {
            return inner.contains(o);
        }

        @Override
        public int indexOf(Object o) {
            return inner.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return inner.lastIndexOf(o);
        }

        @Override
        public boolean isEmpty() {
            return inner.isEmpty();
        }

        @Override
        public boolean containsAll(java.util.Collection<?> c) {
            return inner.containsAll(c);
        }

        public List<E> unmodifiable() {
            return List.copyOf(inner);
        }
    }
}
