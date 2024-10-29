package org.smoodi.core.annotation;

import java.lang.annotation.*;

/**
 * <p>{@link org.smoodi.core.module.loader.ModuleLoader ModuleLoader}에 의해 자동으로 {@link org.smoodi.core.module.container.ModuleContainer IoC 컨테이너}에 등록될 {@link IoC} 객체.</p>
 *
 * @author Daybreak312
 * @see IoC
 * @see org.smoodi.core.module.container.ModuleContainer
 * @see org.smoodi.core.module.loader.ModuleLoader
 * @see StaticModule
 * @see SmoodiDefaultConfiguration
 * @see ModuleInitConstructor
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@IoC
public @interface Module {

    /**
     * <p>동일 타입의 모듈이 다수 존재할 경우, 의존성 주입<sup><i>Dependency Injection</i></sup> 과정에서 사용되는 순서 정보.</p>
     *
     * <p>양수 혹은 {@code 0}이 사용되어야 함.</p>
     * <p>값이 작을 수록 우선순위가 높으며, 동일한 값을 가질 경우 무작위의 순서를 가짐.</p>
     * <p>즉, 매 실행마다 이전과 동일한 순서임이 보장되지 않으므로 안정성을 위해 정확한 순서 지정을 권고.</p>
     *
     * <p>우선순위에 따라 DI에 사용될 모듈이 결정됨.</p>
     * <ul>
     *      <li>단일 모듈이 요구된 경우, 우선순위가 가장 높은 모듈을 제공.</li>
     *      <li>전체 모듈이 요구된 경우, 우선순위에 따라 정렬하여 제공.</li>
     * </ul>
     *
     * <p>{@link Byte#MIN_VALUE}은 감시값<sup><i>Sentinel Value</i></sup>으로, 사용자가 값을 설정하지 않았을 경우에 사용되며 {@code 0}과 같음.</p>
     */
    byte order() default 0;

    /**
     * <p>동일 타입에 여러 모듈이 존재할 때, 의존하는 모듈이 단일 객체를 요구할 경우 우선적으로 주입할 모듈 지정.</p>
     *
     * <p>단일 모듈만 존재할 경우, Primary 모듈을 지정하지 않아도 정상적으로 동작.</p>
     *
     * <p>여러 Primary 모듈이 존재할 경우 {@link org.smoodi.core.module.ModuleDeclareError} 발생.</p>
     *
     * <p>모듈 중 Primary 모듈이 존재하지 않을 경우 {@link org.smoodi.core.module.ModuleDeclareError} 발생.</p>
     *
     * @see org.smoodi.core.module.container.PrimaryModuleFinder
     * @see org.smoodi.core.module.ModuleDeclareError
     */
    boolean isPrimary() default false;

    /**
     * <p>Smoodi IoC 컨테이너에 의해 자동으로 {@link org.smoodi.core.module.container.ModuleContainer ModuleContainer}에 등록할지의 여부</p>
     *
     * <p>{@code false}로 설정할 경우 {@link org.smoodi.core.module.loader.ModuleLoader ModuleLoader}의 스캔 대상에서 제외됨.</p>
     *
     * @see org.smoodi.core.module.loader.ModuleLoader
     * @see org.smoodi.core.module.container.ModuleContainer
     */
    boolean IoC() default true;
}
