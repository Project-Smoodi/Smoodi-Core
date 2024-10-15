package org.smoodi.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.smoodi.annotation.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PackageVerify {

    public static String verify(@NotNull String packageName) {
        assert packageName != null;

        if (packageName.equals("org")) {
            throw new IllegalArgumentException("Package name must not be 'org'.");
        }

        return packageName;
    }
}
