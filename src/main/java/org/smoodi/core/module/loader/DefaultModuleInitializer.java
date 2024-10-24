package org.smoodi.core.module.loader;

import lombok.SneakyThrows;
import org.smoodi.annotation.UseCopy;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.module.ModuleCreationError;
import org.smoodi.core.module.ModuleDeclareError;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.module.container.ModuleContainer;
import org.smoodi.core.util.ModuleUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultModuleInitializer implements ModuleInitializer {

    // Module Container
    private final ModuleContainer mc = SmoodiFramework.getInstance().getModuleContainer();

    @UseCopy
    @SneakyThrows
    @Override
    public void initialize(Set<ModuleType<?>> moduleTypes) {
        moduleTypes = new HashSet<>(moduleTypes);

        initDefaultConstructors(moduleTypes);

        int lastTurnListSize = moduleTypes.size() + 1;

        while (true) {
            if (lastTurnListSize == moduleTypes.size()) {
                ModuleUtils.searchNonModuleDependency(moduleTypes);
                ModuleUtils.searchCircularDependency(moduleTypes);
            }
            lastTurnListSize = moduleTypes.size();

            final Set<ModuleType<?>> initializedModuleTypes = new HashSet<>();

            for (ModuleType<?> moduleType : moduleTypes) {
                if (moduleType.getModuleInitConstructor() == null) {
                    throw new ModuleCreationError("Abstract class or Interface or Enum cannot annotated with " + Module.class + ": " + moduleType.getKlass());
                }

                final List<Object> readyParameters =
                        new ArrayList<>(
                                moduleType.getModuleInitConstructor().getParameterCount());

                for (Class<?> parameterType : moduleType.getModuleInitConstructor().getParameterTypes()) {
                    try {
                        ModuleType.of(parameterType);
                    } catch (IllegalArgumentException e) {
                        throw new ModuleDeclareError("Module's parameter type MUST BE a module too.: " + parameterType.getName(), e);
                    }

                    if (hasUninitializedSubModuleTypes(ModuleType.of(parameterType))) {
                        break;
                    }
                    var foundTypedModule = mc.getPrimaryModuleByClass(parameterType);
                    if (foundTypedModule == null) {
                        break;
                    }
                    readyParameters.add(foundTypedModule);
                }

                if (readyParameters.size() == moduleType.getModuleInitConstructor().getParameterCount()) {
                    try {
                        mc.save(
                                moduleType.getModuleInitConstructor()
                                        .newInstance(readyParameters.toArray())
                        );
                        initializedModuleTypes.add(moduleType);
                    } catch (IllegalArgumentException e) {
                        throw new ModuleCreationError("Invalid parameters entered during constructor call", e);
                    } catch (InstantiationException ignored) {
                        // ModuleType#getModuleInitConstructor가 null이 아닐 경우 인스턴스화 가능한 모듈 타입임을 나타냄.
                        // 고로 인스턴스화 불가능에 의한 예외는 발생할 수 없음.
                    }
                }
            }

            moduleTypes.removeAll(initializedModuleTypes);

            if (moduleTypes.isEmpty()) {
                return;
            }
        }
    }

    @SneakyThrows
    private void initDefaultConstructors(Set<ModuleType<?>> moduleTypes) {

        List<ModuleType<?>> defaultConstructorModuleTypes = new ArrayList<>();

        for (ModuleType<?> moduleType : moduleTypes) {
            if (moduleType.getModuleInitConstructor().getParameterCount() == 0) {
                defaultConstructorModuleTypes.add(moduleType);

            }
        }

        for (ModuleType<?> moduleType : defaultConstructorModuleTypes) {
            moduleTypes.remove(moduleType);
            try {
                mc.save(
                        moduleType.getModuleInitConstructor().newInstance()
                );
            } catch (InstantiationException e) {
                throw new ModuleCreationError("Abstract class or Interface or Enum cannot annotated with " + Module.class + ": " + moduleType.getKlass(), e);
            }
        }
    }

    private boolean hasUninitializedSubModuleTypes(ModuleType<?> moduleType) {
        return moduleType.getSubTypes().stream()
                .anyMatch(it -> it.isCreatable() && it.getPrimaryInstance() == null);
    }
}
