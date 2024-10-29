package org.smoodi.core.annotation;

import java.lang.annotation.*;

/**
 * <p>{@link org.smoodi.core.module.loader.ModuleLoader ModuleLoader}에 의해 자동으로 {@link org.smoodi.core.module.container.ModuleContainer IoC 컨테이너}에 등록될 자바 객체.</p>
 *
 * @author Daybreak312
 * @see org.smoodi.core.module.container.ModuleContainer
 * @see org.smoodi.core.module.loader.ModuleLoader
 * @see StaticModule
 * @see SmoodiDefaultConfiguration
 * @see ModuleInitConstructor
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Module {

    /**
     * <p>Smoodi IoC 컨테이너에 의해 자동으로 {@link org.smoodi.core.module.container.ModuleContainer ModuleContainer}에 등록할지의 여부</p>
     *
     * <p>{@code false}로 설정할 경우 {@link org.smoodi.core.module.loader.ModuleLoader ModuleLoader}의 스캔 대상에서 제외됨.</p>
     *
     * @see org.smoodi.core.module.loader.ModuleLoader
     * @see org.smoodi.core.module.container.ModuleContainer
     */
    boolean IoC() default true;

    /**
     * <p>동일 타입의 모듈이 다수 존재할 경우, {@link Iterable} 등으로 제공될 때 사용되는 순서 정보.</p>
     *
     * <p>양수 혹은 {@code 0}이 사용되어야 함.</p>
     * <p>값이 작을 수록 우선순위가 높으며, 동일한 값을 가질 경우 서로 무작위의 순서를 가짐.</p>
     * <p>즉, 매 실행마다 이전과 동일한 순서임이 보장되지 않으므로 안정성을 위해 정확한 순서 지정을 권고.</p>
     *
     * <p>{@link Byte#MIN_VALUE}은 감시값<sup><i>Sentinel Value</i></sup>으로, 사용자가 값을 설정하지 않았을 경우에 사용되며 {@code 0}과 같음.</p>
     */
    byte order() default 0;

    /**
     * <p>동일 타입에 여러 모듈이 존재할 때, 의존성 주입<sup><i>Dependency Injection</i></sup> 과정에서 우선적으로 주입할 모듈(Primary Module) 지정.</p>
     *
     * <p>단일 모듈만 존재할 경우, Primary 모듈을 지정하지 않아도 정상적으로 동작.</p>
     *
     * <p>여러 Primary 모듈이 존재할 경우 {@link org.smoodi.core.module.ModuleDeclareError} 발생.</p>
     *
     * <p>다수의 모듈 중 Primary 모듈이 존재하지 않을 경우 {@link org.smoodi.core.module.ModuleDeclareError} 발생.</p>
     *
     * @see org.smoodi.core.module.container.PrimaryModuleFinder
     * @see org.smoodi.core.module.ModuleDeclareError
     */
    boolean isPrimary() default false;
}
