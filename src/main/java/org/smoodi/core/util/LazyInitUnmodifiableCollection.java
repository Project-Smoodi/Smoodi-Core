package org.smoodi.core.util;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.annotation.array.UnmodifiableArray;

import java.util.Collection;

/**
 * <p>불변 {@link Collection}을 지연 초기화할 수 있도록 감싼 클래스.</p>
 *
 * <p>{@link Collection} 객체의 불변 상태를 유지하면서, 객체 생성 이후 1회의 초기화 기회가 필요한 상황을 위해 고안됨.</p>
 *
 * <p>아래와 같이 사용.</p>
 * <pre>
 *     private final{@literal LazyInitUnmodifiableCollection<List<Long>>} list
 *         = new LazyInitUnmodifiableCollection<>();
 *
 *    {@literal private void lazyInit(List<Long> values)} {
 *         list.initWith(
 *             List.of(values);
 *         );
 *     }
 * </pre>
 *
 * @param <T> 객체가 감쌀 {@link Collection} 타입
 * @author Daybreak312
 * @see Collection
 * @see java.util.Collections.UnmodifiableCollection
 * @since v0.1.1
 */
public class LazyInitUnmodifiableCollection<T extends Collection<?>> {

    private T collection = null;

    @EmptyableArray
    @UnmodifiableArray
    @NotNull
    public LazyInitUnmodifiableCollection<T> initWith(
            @EmptyableArray
            @UnmodifiableArray
            @NotNull
            T collection
    ) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection is null");
        }
        if (isInitialized()) {
            throw new IllegalStateException("Collection has already been initialized");
        }

        try {
            collection.add(null);
        } catch (UnsupportedOperationException e) {
            this.collection = collection;
            return this;
        } catch (Throwable e) {
            throw new IllegalArgumentException("Only unmodifiable collections are supported");
        }

        throw new IllegalArgumentException("Only unmodifiable collections are supported");
    }

    @NotNull
    public boolean isInitialized() {
        return collection != null;
    }

    @EmptyableArray
    @UnmodifiableArray
    @NotNull
    public T get() {
        if (!isInitialized()) {
            throw new IllegalStateException("LazyInitUnmodifiableCollection not initialized");
        }

        return collection;
    }
}
