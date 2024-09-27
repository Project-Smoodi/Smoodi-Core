package org.smoodi.core.util;

import org.smoodi.annotation.Api;
import org.smoodi.annotation.NotNull;
import org.smoodi.annotation.Nullable;

public class NullChecker {

    public static NullChecker checker() {
        return new NullChecker();
    }

    @NotNull
    @Api
    public NullChecker notNull(@Nullable Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return this;
    }
}
