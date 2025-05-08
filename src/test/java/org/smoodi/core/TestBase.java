package org.smoodi.core;

import org.smoodi.core.init.LoggerInitializer;
import org.smoodi.core.lifecycle.Lifecycle;

import java.lang.reflect.Field;

public final class TestBase {

    private TestBase() {
    }

    public static void initWith(final Class<?> mainClass) {
        initLogger();

        resetLifecycle();

        injectMainClass(mainClass);
    }

    private static void resetLifecycle() {
        if (SmoodiFramework.getInstance().getState() != Lifecycle.State.SLEEPING) {
            SmoodiFramework.kill();
            SmoodiFramework.getInstance().setState(Lifecycle.State.SLEEPING);
        }
    }

    private static void initLogger() {
        LoggerInitializer.configureLogback();
    }

    private static void injectMainClass(final Class<?> mainClass) {
        try {
            final Field field = SmoodiFramework.class.getDeclaredField("mainClass");
            field.setAccessible(true);
            field.set(SmoodiFramework.getInstance(), mainClass);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
        }
    }
}