package org.smoodi.core.module;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.annotation.array.UnmodifiableArray;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * <p>특정 모듈을 초기화하기 위한 생성자</p>
 *
 * <p>{@link Constructor Pure Java Constructor}의 Wrapper. 의존성을 보다 편하게 관리하기 위해 작성됨.</p>
 *
 * @param <T>이 생성자를 갖고 있는 자바 클래스 타입
 * @param <M>  {@link T}의 {@link ModuleType}
 * @author Daybreak312
 * @since 0.1.5-SNAPSHOT
 */
public interface ModuleInitConstructor<T, M extends ModuleType<T>> {

    @NotNull
    M getModuleType();

    @NotNull
    Constructor<T> getJavaConstructor();

    @NotNull
    @EmptyableArray
    @UnmodifiableArray
    List<ModuleDependency<M, ?>> getDependencies();

    static <T, M extends ModuleType<T>> ModuleInitConstructor<T, M> of(M moduleType) {
        return ModuleInitConstructorImpl.of(moduleType);
    }
}
