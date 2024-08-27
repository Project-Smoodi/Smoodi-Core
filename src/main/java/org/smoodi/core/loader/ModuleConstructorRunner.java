package org.smoodi.core.loader;

import lombok.SneakyThrows;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.context.ModuleContainer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ModuleConstructorRunner {

    // Module Container
    private final ModuleContainer mc = SmoodiFramework.getModuleContainer();

    @SneakyThrows
    public void create(List<Constructor<?>> constructors) {
        createDefaultConstructors(constructors);

        int lastTurnListSize = constructors.size() + 1;

        while (true) {
            if (lastTurnListSize == constructors.size()) {
                throw new IllegalStateException("Circular reference found on " + constructors.size() + " modules : " + constructors);
            }
            lastTurnListSize = constructors.size();

            final List<Constructor<?>> remove = new ArrayList<>();

            for (Constructor<?> constructor : constructors) {
                final List<Object> parameters = new ArrayList<>(constructors.size());

                for (Class<?> parameterType : constructor.getParameterTypes()) {
                    var parameter = mc.getPrimaryModuleByClass(parameterType);
                    if (parameter == null) {
                        break;
                    }
                    parameters.add(parameter);
                }

                if (parameters.size() == constructor.getParameterCount()) {
                    try {
                        mc.save(
                                constructor.newInstance(parameters.toArray())
                        );
                        remove.add(constructor);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalStateException("Invalid parameters entered during constructor call", e);
                    }
                }
            }

            constructors.removeAll(remove);

            if (constructors.isEmpty()) {
                return;
            }
        }
    }

    @SneakyThrows
    private void createDefaultConstructors(List<Constructor<?>> constructors) {

        List<Constructor<?>> defaultConstructors = new ArrayList<>();

        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                defaultConstructors.add(constructor);
                break;
            }
        }

        for (Constructor<?> constructor : defaultConstructors) {
            constructors.remove(constructor);
            mc.save(
                    constructor.newInstance()
            );
        }
    }
}
