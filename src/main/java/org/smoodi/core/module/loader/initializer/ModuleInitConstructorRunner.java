package org.smoodi.core.module.loader.initializer;

import lombok.SneakyThrows;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.module.ModuleCreationError;
import org.smoodi.core.module.container.ModuleContainer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ModuleInitConstructorRunner {

    // Module Container
    private final ModuleContainer mc = SmoodiFramework.getModuleContainer();

    @SneakyThrows
    public void runConstructor(List<Constructor<Object>> constructors) {
        initDefaultConstructors(constructors);

        int lastTurnListSize = constructors.size() + 1;

        while (true) {
            if (lastTurnListSize == constructors.size()) {
                throw new ModuleCreationError("Circular reference found on " + constructors.size() + " modules : " + constructors);
            }
            lastTurnListSize = constructors.size();

            final List<Constructor<Object>> initializedConstructors = new ArrayList<>();

            for (Constructor<Object> constructor : constructors) {
                final List<Object> preparatoryParameters = new ArrayList<>(constructors.size());

                for (Class<?> parameterType : constructor.getParameterTypes()) {
                    var foundTypedModule = mc.getPrimaryModuleByClass(parameterType);
                    if (foundTypedModule == null) {
                        break;
                    }
                    preparatoryParameters.add(foundTypedModule);
                }

                if (preparatoryParameters.size() == constructor.getParameterCount()) {
                    try {
                        mc.save(
                                constructor.newInstance(preparatoryParameters.toArray())
                        );
                        initializedConstructors.add(constructor);
                    } catch (IllegalArgumentException e) {
                        throw new ModuleCreationError("Invalid parameters entered during constructor call", e);
                    }
                }
            }

            constructors.removeAll(initializedConstructors);

            if (constructors.isEmpty()) {
                return;
            }
        }
    }

    @SneakyThrows
    private void initDefaultConstructors(List<Constructor<Object>> constructors) {

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
