package org.smoodi.core.module;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * <p>상속 관계 정보를 포함한 {@link ModuleType}.</p>
 *
 * <p>특정 타입에 대해 대응 가능한 모든 {@link org.smoodi.core.annotation.Module 모듈}이 모두 초기화되었는지 확인하기 위해 사용.</p>
 *
 * @author Daybreak312
 * @see org.smoodi.core.annotation.Module
 * @see Class
 * @see ModuleType
 */
public final class HierarchyModuleType extends ModuleType {

    private final Set<ModuleType> lowerTypes;

    private int initializedLowerModuleTypes = 0;

    private HierarchyModuleType(
            Class<?> klass,
            boolean isSingleton,
            Set<ModuleType> lowerTypes
    ) {
        super(klass, isSingleton);
        this.lowerTypes = lowerTypes;
    }

    private HierarchyModuleType(
            Class<?> klass,
            boolean isSingleton,
            Constructor<?> moduleInitConstructor,
            Set<ModuleType> lowerTypes
    ) {
        super(klass, isSingleton, moduleInitConstructor);
        this.lowerTypes = lowerTypes;
    }

    public static HierarchyModuleType of(Class<?> klass) {
        return new HierarchyModuleType(klass, false, null);
    }


    public void markInit(ModuleType type) {
        if (this.lowerTypes.contains(type)) {
            this.initializedLowerModuleTypes++;
        }
    }
}
