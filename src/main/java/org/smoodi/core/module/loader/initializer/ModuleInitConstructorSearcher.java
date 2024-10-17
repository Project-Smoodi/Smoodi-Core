package org.smoodi.core.module.loader.initializer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.smoodi.core.annotation.ModuleInitConstructor;
import org.smoodi.core.module.ModuleDeclareError;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.util.AnnotationUtils;

import java.lang.reflect.Constructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModuleInitConstructorSearcher {

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> findModuleInitConstructor(ModuleType<T> moduleType) {
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
