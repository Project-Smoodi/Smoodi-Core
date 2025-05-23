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

    @NotNull
    Class<T> getKlass();

    @Nullable
    ModuleInitConstructor<T, ModuleType<T>> getModuleInitConstructor();

    void markAsInstanceCreated(T primaryInstance);

    @Nullable
    T getPrimaryInstance();

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
