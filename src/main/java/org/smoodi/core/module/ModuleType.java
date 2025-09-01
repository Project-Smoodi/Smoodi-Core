package org.smoodi.core.module;

import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;
import org.smoodi.annotation.array.EmptyableArray;
import org.smoodi.annotation.array.UnmodifiableArray;
import org.smoodi.core.annotation.Module;
import org.smoodi.core.util.AnnotationUtils;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * <p>{@link org.smoodi.core.annotation.Module 모듈} 초기화에서 요구되는 {@link Class 클래스 정보}와 기타 추가적인 정보를 묶은 클래스.</p>
 *
 * @author Daybreak312
 * @see org.smoodi.core.annotation.Module
 * @see Class
 * @since 0.1.5-SNAPSHOT
 */
public interface ModuleType<T> {

    /**
     * <p>모듈의 실제 Java 런타임 클래스(Klass).</p>
     *
     * @return Java 클래스
     */
    @NotNull
    Class<T> getKlass();

    /**
     * <p>해당 모듈을 초기화하기 위한 생성자.</p>
     *
     * @return 생성자, 만약 인스턴스화할 수 없는 모듈 타입이라면 {@code null}
     * @see ModuleType#isInstantiableKlass()
     */
    @Nullable
    ModuleInitConstructor<T, ModuleType<T>> getModuleInitConstructor();

    /**
     * <p>모듈 컨테이너의 초기화 과정 중에, 해당 모듈 타입을 통해 주입되는 인스턴스 객체가 초기화된 경우, 해당 객체를 이 모듈 타입에 등록하고 초기화되었음을 표시.</p>
     *
     * @param primaryInstance 생성자에서 주입될 인스턴스 객체
     */
    void markAsInstanceCreated(T primaryInstance);

    /**
     * <p>모듈 컨테이너에 존재하는, 이 모듈 타입의 인스턴스 객체 중 실제로 주입될 객체.</p>
     *
     * @return 객체, 만약 없거나 아직 초기화되지 않은 경우 {@code null}
     */
    @Nullable
    T getPrimaryInstance();

    /**
     *
     * @return
     */
    @NotNull
    @EmptyableArray
    @UnmodifiableArray
    List<ModuleType<? extends T>> getSubTypes();

    static boolean isKlassModule(@NotNull Class<?> klass) {
        if (isInstantiableKlass(klass)) {
            return AnnotationUtils.findIncludeAnnotation(klass, Module.class) != null;
        } else return klass.isInterface() || Modifier.toString(klass.getModifiers()).contains("abstract");
    }

    static boolean isInstantiableKlass(@NotNull Class<?> klass) {
        assert klass != null;

        return !klass.isAnnotation() &&
                !klass.isEnum() && !klass.isInterface() &&
                !klass.isPrimitive() && !klass.isAnonymousClass() &&
                !Modifier.toString(klass.getModifiers()).contains("abstract");
    }

    default boolean isInstantiableKlass() {
        return isInstantiableKlass(getKlass());
    }

    @NotNull
    static <T> ModuleType<T> of(@NotNull Class<T> klass) {
        return ModuleTypeImpl.of(klass);
    }
}
