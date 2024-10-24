package org.smoodi.core;

import org.smoodi.core.init.LoggerInitializer;

import java.lang.reflect.Field;

public final class TestBase {

    private TestBase() {
    }

    public static void initTest() {
        LoggerInitializer.configureLogback();
    }

    public static void setMainClass(final Class<?> mainClass) {
        try {
            final Field field = SmoodiFramework.class.getDeclaredField("mainClass");
            field.setAccessible(true);
            field.set(SmoodiFramework.getInstance(), mainClass);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
        }
    }
}