package org.smoodi.core.loader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.context.ModuleContainer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModuleConstructorRunner {

    // Module Container
    private static final ModuleContainer mc = SmoodiFramework.getModuleContainer();


    @SneakyThrows
    public void create(List<Constructor<?>> constructors) {
        createEmptyConstructor(constructors);

        int lastTurnListSize = constructors.size() + 1;

        while (true) {
            if (lastTurnListSize == constructors.size()) {
                throw new IllegalStateException("Circular reference found");
            }

            for (Constructor<?> constructor : constructors) {
                final List<Object> parameters = new ArrayList<>(constructors.size());

                for (Class<?> parameterType : constructor.getParameterTypes()) {
                    var parameter = mc.getPrimaryModuleByClass(parameterType);
                    if (parameter == null) {
                        break;
                    }
                    parameters.add(parameter);
                }

                if (parameters.size() == constructors.size()) {
                    try {
                        mc.save(
                                constructor.newInstance(parameters)
                        );
                        constructors.remove(constructor);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalStateException("Invalid parameters entered during constructor call");
                    }
                }
            }

            if (constructors.isEmpty()) {
                return;
            }

            lastTurnListSize = constructors.size();
        }
    }

    @SneakyThrows
    private void createEmptyConstructor(List<Constructor<?>> constructors) {
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                constructors.remove(constructor);
                mc.save(
                        constructor.newInstance()
                );
            }
        }
    }

    @Getter
    private static final ModuleConstructorRunner instance = new ModuleConstructorRunner();
}
