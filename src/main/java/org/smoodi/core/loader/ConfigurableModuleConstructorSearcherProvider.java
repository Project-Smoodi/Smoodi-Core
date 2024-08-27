package org.smoodi.core.loader;

import org.smoodi.core.SmoodiStarter;

import java.lang.reflect.Constructor;

public class ConfigurableModuleConstructorSearcherProvider implements ModuleConstructorSearcher {

    private ModuleConstructorSearcher cache = null;

    @Override
    public Constructor<?> findConstructor(Class<?> klass) {
        if (cache != null) {
            return cache.findConstructor(klass);
        }

        ModuleConstructorTarget targeting =
                SmoodiStarter.mainClass.getAnnotation(ModuleConstructorTarget.class);

        if (targeting == null) {
            throw new IllegalStateException("ModuleConstructorTarget annotation not found in main class " + SmoodiStarter.mainClass.getName());
        }

        switch (targeting.target()) {
            case EMPTY_CONSTRUCTOR:
                cache = new EmptyModuleConstructorSearcher();
                break;
            case ANNOTATED:
                cache = new AnnotatedModuleConstructorSearcher();
                break;
            default:
                cache = new DefaultModuleConstructorSearcher();
        }

        return cache.findConstructor(klass);
    }
}
