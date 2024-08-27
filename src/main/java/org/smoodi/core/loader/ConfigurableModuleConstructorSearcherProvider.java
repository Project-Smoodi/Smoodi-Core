package org.smoodi.core.loader;

import org.smoodi.core.SmoodiStarter;

import java.lang.reflect.Constructor;

public class ConfigurableModuleConstructorSearcherProvider implements ModuleConstructorSearcher {

    private ModuleConstructorSearcher cache = null;

    @Override
    public Constructor<?> findModuleInitConstructor(Class<?> klass) {
        if (cache != null) {
            return cache.findModuleInitConstructor(klass);
        }

        ModuleInitConstructorTargeter targeting =
                SmoodiStarter.mainClass.getAnnotation(ModuleInitConstructorTargeter.class);

        if (targeting == null) {
            throw new IllegalStateException("ModuleInitConstructorTargeter annotation not found in main class " + SmoodiStarter.mainClass.getName());
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

        return cache.findModuleInitConstructor(klass);
    }
}
