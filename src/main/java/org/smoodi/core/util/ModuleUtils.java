package org.smoodi.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.module.ModuleType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModuleUtils {

    private static final List<Reflections> reflections;

    static {
        if (SmoodiFramework.getMainClass().getPackage().getName().startsWith(
                SmoodiFramework.SMOODI_PACKAGE_PREFIX)) {
            reflections = List.of(
                    new Reflections(SmoodiFramework.SMOODI_PACKAGE_PREFIX)
            );
        } else {
            reflections = List.of(
                    new Reflections(SmoodiFramework.getMainClass().getPackage().getName()),
                    new Reflections(SmoodiFramework.SMOODI_PACKAGE_PREFIX)
            );
        }
    }

    public static <T> Set<ModuleType<? extends T>> getModuleSubTypes(Class<T> klass) {

        final Set<Class<? extends T>> subTypes = new HashSet<>();

        for (Reflections reflection : reflections) {
            subTypes.addAll(reflection.getSubTypesOf(klass));
        }

        // Set to unmodifiable
        return subTypes.stream()
                .map(ModuleType::of)
                .collect(Collectors.toSet());
    }
}
