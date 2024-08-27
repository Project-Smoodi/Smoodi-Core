package org.smoodi.core.loader;

import org.smoodi.core.SmoodiStarter;
import org.smoodi.core.config.ConfigurationError;

import java.lang.reflect.Constructor;
import java.util.Set;

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
    public Constructor<?> findModuleInitConstructor(Class<?> klass) {
        if (cache != null) {
            return findVerifiedModuleInitConstructor(klass);
        }

        ModuleInitConstructorTargeter targeting =
                SmoodiStarter.mainClass.getAnnotation(ModuleInitConstructorTargeter.class);

        if (targeting == null) {
            throw new ConfigurationError("ModuleInitConstructorTargeter annotation not found in main class " + SmoodiStarter.mainClass.getName());
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

        return findVerifiedModuleInitConstructor(klass);
    }

    private Constructor<?> findVerifiedModuleInitConstructor(Class<?> klass) {
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
