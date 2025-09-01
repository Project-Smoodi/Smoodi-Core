package org.smoodi.core.module;

/**
 * <p>IoC 컨테이너에 저장되는 객체의 Wrapper.</p>
 *
 * @param <T> 실제 Java 타입.
 */
public interface Module<T> {

    Class<T> getKlass();

    ModuleType<T> getModuleType();

    T getInstance();
}
