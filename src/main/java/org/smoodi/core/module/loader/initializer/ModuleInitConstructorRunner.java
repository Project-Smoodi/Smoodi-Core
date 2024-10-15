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
    private final ModuleContainer mc = SmoodiFramework.getInstance().getModuleContainer();

    @SneakyThrows
    public void runConstructor(List<Constructor<?>> constructors) {
        initDefaultConstructors(constructors);

        int lastTurnListSize = constructors.size() + 1;

        while (true) {
            if (lastTurnListSize == constructors.size()) {
                CircularDependencySearch.search(constructors);
            }
            lastTurnListSize = constructors.size();

            final List<Constructor<?>> initializedConstructors = new ArrayList<>();

            for (Constructor<?> constructor : constructors) {
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
                    } catch (InstantiationException e) {
                        throw new ModuleCreationError("Abstract class or Interface or Enum cannot annotated with " + Module.class + ": " + constructor.getDeclaringClass(), e);
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
    private void initDefaultConstructors(List<Constructor<?>> constructors) {

        List<Constructor<?>> defaultConstructors = new ArrayList<>();

        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                defaultConstructors.add(constructor);
            }
        }

        for (Constructor<?> constructor : defaultConstructors) {
            constructors.remove(constructor);
            try {
                mc.save(
                        constructor.newInstance()
                );
            } catch (InstantiationException e) {
                throw new ModuleCreationError("Abstract class or Interface or Enum cannot annotated with " + Module.class + ": " + constructor.getDeclaringClass(), e);
            }
        }
    }
}
