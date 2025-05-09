package org.smoodi.core.module;

import lombok.Getter;
import org.smoodi.core.util.ModuleUtils;

/**
 * @param <T> {@link M}의 제네릭 타입 -> X
 * @param <M> 이 의존성을 가지고 있는 {@link ModuleType} -> {@link ModuleDependency super.T}
 * @param <D> 생성자에 명시된 원본 타입의 {@link ModuleType} -> {@link ModuleDependency super.D}
 */
public class ModuleDependencyImpl<T, M extends ModuleType<? extends T>, D>
        implements ModuleDependency<M, D> {

    @Getter(onMethod_ = {@Override})
    private final M moduleType;

    @Getter(onMethod_ = {@Override})
    private final Class<D> dependencyType;

    private ModuleType<? extends D> moduleTypeForInjection;

    public ModuleType<? extends D> getModuleTypeForInjection() {
        if (moduleTypeForInjection == null) {
            moduleTypeForInjection = ModuleUtils.findPrimaryModuleType(ModuleType.of(dependencyType));

            if (this.moduleTypeForInjection == null) {
                throw new ModuleDeclareError("Super type \"" + dependencyType.getName() + " doesnt have any instantiable class.");
            }
        }

        return this.moduleTypeForInjection;
    }

    /**
     * @param moduleType     이 의존성을 갖고 있는 {@link ModuleType}
     * @param dependencyType 이 의존성이 참조하는 클래스 타입
     */
    public static <T, M extends ModuleType<? extends T>, D> ModuleDependencyImpl<T, M, D> of(
            M moduleType, Class<D> dependencyType
    ) {
        return new ModuleDependencyImpl<>(moduleType, dependencyType);
    }

    /**
     * @param moduleType     이 의존성을 갖고 있는 {@link ModuleType}
     * @param dependencyType 이 의존성이 참조하는 클래스 타입
     */
    public ModuleDependencyImpl(M moduleType, Class<D> dependencyType) {
        this.moduleType = moduleType;

        if (!ModuleType.isKlassModule(dependencyType))
            throw new ModuleDeclareError("Module cannot depend non-module type: dependency type \"" + dependencyType.getName() + "\" of module type \"" + moduleType.getKlass().getName() + "\"");
        this.dependencyType = dependencyType;
    }
}
