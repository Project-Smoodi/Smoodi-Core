package org.smoodi.core.module.loader;

import lombok.SneakyThrows;
import org.smoodi.annotation.UseCopy;
import org.smoodi.core.SmoodiFramework;
import org.smoodi.core.module.ModuleCreationError;
import org.smoodi.core.module.ModuleDependency;
import org.smoodi.core.module.ModuleType;
import org.smoodi.core.module.container.ModuleContainer;
import org.smoodi.core.util.ModuleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultModuleInitializer implements ModuleInitializer {

    // Module Container
    private final ModuleContainer mc = SmoodiFramework.getInstance().getModuleContainer();

    @UseCopy
    @SneakyThrows
    @Override
    public void initialize(List<ModuleType<?>> moduleTypes) {
        initDefaultConstructors(moduleTypes);

        int lastTurnListSize = moduleTypes.size() + 1;

        while (true) {
            if (lastTurnListSize == moduleTypes.size()) {
                ModuleUtils.searchCircularDependency(moduleTypes);
            }
            lastTurnListSize = moduleTypes.size();

            final List<ModuleType<?>> initializedModuleTypes = new ArrayList<>();

            for (ModuleType<?> moduleType : moduleTypes) {
                if (moduleType.getModuleInitConstructor() == null) {
                    throw new ModuleCreationError("Abstract class or Interface or Enum cannot annotated with " + Module.class + ": " + moduleType.getKlass());
                }

                final List<Object> readyParameters = new ArrayList<>(
                        moduleType.getModuleInitConstructor().getDependencies().size()
                );

                for (ModuleDependency<?, ?> dependency : moduleType.getModuleInitConstructor().getDependencies()) {
                    if (hasUninitializedSubModuleTypes(dependency.getModuleTypeForInjection())) {
                        break;
                    }
                    var foundTypedModule = mc.getPrimaryModuleByClass(dependency.getDependencyType());
                    if (foundTypedModule == null) {
                        break;
                    }
                    readyParameters.add(foundTypedModule);
                }

                if (readyParameters.size() == moduleType.getModuleInitConstructor().getDependencies().size()) {
                    try {
                        mc.save(
                                moduleType.getModuleInitConstructor().getJavaConstructor()
                                        .newInstance(readyParameters.toArray())
                        );
                        initializedModuleTypes.add(moduleType);
                    } catch (IllegalArgumentException e) {
                        throw new ModuleCreationError("Invalid parameters entered during constructor call: " + moduleType.getKlass().getName() + " - " + readyParameters, e);
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
    private void initDefaultConstructors(List<ModuleType<?>> moduleTypes) {

        List<ModuleType<?>> defaultConstructorModuleTypes = new ArrayList<>();

        for (ModuleType<?> moduleType : moduleTypes) {
            if (moduleType.getModuleInitConstructor() == null) {
                continue;
            }
            if (moduleType.getModuleInitConstructor().getDependencies().isEmpty()) {
                defaultConstructorModuleTypes.add(moduleType);
            }
        }

        for (ModuleType<?> moduleType : defaultConstructorModuleTypes) {
            moduleTypes.remove(moduleType);
            try {
                mc.save(
                        Objects.requireNonNull(
                                moduleType.getModuleInitConstructor()
                        ).getJavaConstructor().newInstance()
                );
            } catch (InstantiationException e) {
                throw new ModuleCreationError("Abstract class or Interface or Enum cannot annotated with " + Module.class + ": " + moduleType.getKlass(), e);
            }
        }
    }

    private boolean hasUninitializedSubModuleTypes(ModuleType<?> moduleType) {
        return moduleType.getSubTypes().stream()
                .anyMatch(it -> moduleType.isInstantiableKlass() && it.getPrimaryInstance() == null);
    }
}
