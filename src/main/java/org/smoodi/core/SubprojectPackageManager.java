package org.smoodi.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubprojectPackageManager {

    private static final Map<String, Package> subprojects = new HashMap<>();

    private static final String SMOODI_PACKAGE_PREFIX = "org.smoodi";

    public static void addSubproject(String s, Package p) {
        if (!p.getName().startsWith(SMOODI_PACKAGE_PREFIX)) {
            throw new IllegalArgumentException("Only smoodi project packages are supported");
        }

        subprojects.put(s, p);
    }

    public static Map<String, Package> getSubprojects() {
        return Map.copyOf(subprojects);
    }
}
