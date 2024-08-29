package org.smoodi.core.module.loader.initializer;

import lombok.extern.slf4j.Slf4j;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.config.ConfigurationError;
import org.smoodi.core.module.ModuleDeclareError;
import org.smoodi.core.module.ModuleInitConstructorTargeting;

import java.lang.reflect.Constructor;
import java.util.Set;

@Slf4j
public class ConfigurableModuleInitConstructorSearcherProvider implements ModuleInitConstructorSearcher {

    private ModuleInitConstructorSearcher cache = null;

    private static final Set<Class<?>> WRAPPER_TYPES = Set.of(
            Integer.class,
            Long.class,
            Short.class,
            Byte.class,
            Double.class,
            Float.class,
            Character.class,
            Boolean.class,
            String.class
    );

    @Override
    public Constructor<Object> findModuleInitConstructor(Class<Object> klass) {
        if (cache != null) {
            return findVerifiedModuleInitConstructor(klass);
        }

        ModuleInitConstructorTargeting targeting =
                SmoodiFramework.getMainClass().getAnnotation(ModuleInitConstructorTargeting.class);

        if (targeting == null) {
            throw new ConfigurationError(ModuleInitConstructorTargeting.class.getSimpleName() + " annotation not found in main class " + SmoodiFramework.getMainClass().getName());
        }

        switch (targeting.target()) {
            case EMPTY_CONSTRUCTOR:
                cache = new EmptyModuleInitConstructorSearcher();
                break;
            case ANNOTATED:
                cache = new AnnotatedModuleInitConstructorSearcher();
                break;
            default:
                cache = new DefaultModuleInitConstructorSearcher();
        }

        log.info("Module initialization constructor selected by strategy {}", targeting.target().name());

        return findVerifiedModuleInitConstructor(klass);
    }

    private Constructor<Object> findVerifiedModuleInitConstructor(Class<Object> klass) {
        var constructor = cache.findModuleInitConstructor(klass);

        for (Class<?> parameterType : constructor.getParameterTypes()) {
            if (parameterType.isPrimitive() || WRAPPER_TYPES.contains(parameterType)) {
                throw new ModuleDeclareError(
                        "Module's init constructor CANNOT have any primitive or wrapper types. class: \"" + klass.getName() + "\" with parameter type: \"" + parameterType + "\"");
            }
        }

        return constructor;
    }
}
