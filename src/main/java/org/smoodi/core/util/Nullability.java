package org.smoodi.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.smoodi.annotation.Api;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Nullability {

    @NotNull
    @Api
    public static <T> T notNull(@Nullable final T value) {
         if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }

    @Api
    public static void notNull(@Nullable final Object... objects) {
        for (Object object : objects) {
            notNull(object);
        }
    }

    public <T> T firstOrSecondIfNull(final T first, final T second) {
        return first == null ? second : first;
    }
}
