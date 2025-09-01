package org.smoodi.docs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 이 프로젝트의 주요 개념, 설정 등 기타 여러 정보에 대한 문서를 정리하기 위한 클래스입니다.
 * <p>
 * 여러 범위에 걸쳐 퍼져 있는 주석과 개념 설명을 한 곳에 통합하기 위해 만들어졌습니다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoreDocs {

    /**
     * 모듈이란, {@link IocContainerDocs IoC 컨테이너}에서 인지하고 있는, 프레임워크에 의해 관리되는 '객체'를 의미합니다.
     * <p>
     * 보다 정확히는 객체의 생성과 사용, 참조 해제에 이르는 {@link IocContainerDocs.ObjectLifecycleDocs 객체 생명주기}의 일부를
     * 프레임워크가 대신 관리하며(제어의 역전, IoC), 생성 시점에 의존성 주입(Dependency Injection)을 제공하는 객체를 말합니다.
     * <p>
     * 사용자는 클래스를 정의하고 {@link org.smoodi.core.annotation.Module 전용 어노테이션}을 부착하기만 하면, 모듈 로더가 자동으로 인스턴스화하고 IoC 컨테이너에 등록합니다.
     *
     * <h3>모듈의 특징</h3>
     * 모든 모듈은 아래와 같은 특징을 가지며, 이를 준수해야 합니다.
     * <ol>
     *     <li><b>Immutable(불변)</b>: 모듈의 상태(필드)는 생성 이후 변하지 않습니다. 의존성을 담는 필드 또한 동일합니다.</li>
     *     <li><b>Stateless(무상태)</b>: 메시징의 이력이나 상태를 저장하지 않습니다. 한 번의 메시징이 다른 메시징에 영향을 주지 않습니다.</li>
     *     <li><b>NullSafe(null에 안전)</b>: 구성 및 메시징 파라미터에 null을 사용하지 않습니다. 필요한 경우 의미 있는 감시 값(Sentinel Value)을 권장합니다.</li>
     * </ol>
     * 프레임워크는 위 가정이 충족된다는 전제하에 동작하며, 사용자 역시 동일한 원칙에 따라 모듈을 설계해야 합니다.
     *
     * <h3>인스턴스화 가능한 타입과 모듈</h3>
     * 인터페이스나 추상 클래스처럼 직접 인스턴스화할 수 없는 타입도 모듈이 될 수 있습니다. 다시 말해, 모든 모듈이 인스턴스화할 수 있는 건 아닙니다.<br>
     * 이는 DIP(Dependency Inversion Principle), 의존성 역전 원칙에 따라, 실제 구현체가 IoC 컨테이너에 등록되어 있으면 주입이 가능하기 때문입니다.<br>
     * 자세한 내용은 {@link DependencyInjectionDocs 의존성 주입}을 참고하세요.
     * <p>
     *
     * <h3>의존성 주입과 모듈 판단 방식</h3>
     * 모듈의 생성자 매개변수는 모두 의존성 주입 대상이며, IoC 컨테이너에 등록된 모듈이어야 합니다.<br>
     * 의존성 주입 대상이 되는 모듈은 다음과 같은 기준에 따라 판단합니다.
     * <ul>
     *     <li>생성자 주입 대상이 인스턴스화 가능한 타입이고 {@code @Module}이 부착되어 있다면 모듈로 판단합니다.</li>
     *     <li>인스턴스화 가능한 타입인데 {@code @Module}이 없다면 일반 클래스(String 등)로 간주하여 주입 대상에서 제외합니다.</li>
     *     <li>인터페이스/추상 클래스처럼 인스턴스화 불가능한 타입은 DIP 원칙에 따라 별도 조치 없이 통과되며, 실제 구현체 모듈이 IoC 컨테이너에 등록되어 있어야 주입이 가능합니다.</li>
     * </ul>
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ModuleDocs {

        /**
         * 의존성 주입<sup>Dependency Injection</sup>이란, 객체가 필요로 하는 의존성을 외부에서 주입하는 설계 패턴입니다.
         * <p>
         * 여기에서 의존성이란, 객체가 자신의 기능을 수행하기 위해 필요로 하는 다른 객체를 의미합니다.<br>
         * 이러한 의존성은 IoC 컨테이너에 의해 관리되며, 객체가 생성될 때 자동으로 주입됩니다.<br>
         * 또한 모든 의존성은 IoC 컨테이너에 의해 관리되는, 즉 모듈이여야 합니다.
         * <p>
         * Smoodi에서는 모듈의 생성자 매개변수를 통해 의존성 주입을 구현합니다.<br>
         * 의존성 그래프를 분석하여 적절한 순서로 모듈을 인스턴스화하고, 필요한 의존성을 생성자에 주입합니다.
         * <p>
         * 여기에서 Smoodi는 DIP<sup>Dependency Inversion Principle</sup>, 의존성 역전 원칙을 구현했습니다.<br>
         * 다시 말해, 실제로 인스턴스화가 가능한 구체 클래스가 아닌, 인터페이스나 추상 클래스를 통해서도 의존성을 선언할 수 있습니다.
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class DependencyInjectionDocs {
        }

        /**
         * 모듈 초기화 생성자<sup>Module Initialization Constructor</sup>란, 모듈이 인스턴스화될 때 호출되는 생성자를 의미합니다.
         * <p>
         * 모듈의 생성자 매개변수는 모두 의존성 주입 대상이며, IoC 컨테이너에 등록된 모듈이어야 합니다.<br>
         * 의존성 주입 대상이 되는 모듈은 {@link ModuleDocs 의존성 주입}에서 설명한 바와 같은 기준에 따라 판단합니다.
         * <p>
         * 모듈 초기화 생성자는 아래의 기준에 따라 선택합니다.
         * <ol>
         *     <li>모듈에 생성자가 하나뿐이라면, 해당 생성자를 모듈 초기화 생성자로 선택합니다.</li>
         *     <li>모듈에 생성자가 둘 이상이라면, {@link org.smoodi.core.annotation.ModuleInitConstructor @ModuleInitConstructor} 어노테이션이 부착된 생성자를 선택합니다.</li>
         *     <li>만약 어노테이션이 없을 경우, 매개변수가 없는 기본 생성자를 선택합니다.</li>
         *     <li>기본 생성자도 없을 경우, {@link org.smoodi.core.module.ModuleDeclareError 모듈 선언 오류}가 발생합니다.</li>
         * </ol>
         * 생성자의 접근 제어자는
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class ModuleInitConstructorDocs {
        }

        /**
         * {@link org.smoodi.core.annotation.Module @Module} 어노테이션과 그 매개변수에 대한 설명입니다.
         * <p>
         * 클래스(또는 어노테이션)에 부착하여 모듈로 등록함을 나타냅니다.<br>
         * 부착된 타입은 모듈 로더에 의해 자동으로 인스턴스화되고 IoC 컨테이너에 저장됩니다.<br>
         * <pre>
         * boolean IoC() default true;
         * </pre>
         * 모듈 로더가 해당 타입을 자동으로 인스턴스화하고 IoC 컨테이너에 등록할지 여부를 지정합니다.<br>
         * 기본값은 true이며, false로 설정하면 해당 타입을 모듈로 인식은 하지만, 자동 인스턴스화 대상에서는 제외합니다.<br>
         * 이는 {@link StaticModuleDocs 정적 모듈} 등 특수한 상황을 위한 옵션입니다.
         * <p>
         * <pre>
         * byte order() default 0;
         * </pre>
         * 동일한 타입에 여러 모듈이 존재할 때, 이 모듈이 우선적으로 선택될 순서를 지정합니다.<br>
         * 값이 낮을수록 우선순위가 높으며, 기본값은 0입니다.<br>
         * 자세한 내용은 {@link OrderedModulesDocs 순서 지정 모듈}을 참고하세요.<br>
         * <p>
         * <pre>
         * boolean isPrimary() default false;
         * </pre>
         * 동일한 타입에 여러 모듈이 존재할 때, 이 모듈이 기본 모듈<sup>Primary Module</sup>임을 지정합니다.<br>
         * 기본값은 false이며, 단일 모듈만 존재할 때는 지정하지 않아도 무방합니다.<br>
         * 자세한 내용은 {@link PrimaryModuleDocs 기본 모듈}을 참고하세요.
         *
         * @see org.smoodi.core.annotation.Module
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class AnnotationDocs {
        }

        /**
         * 기본 모듈<sup>Primary Module</sup>이란, 동일한 타입에 여러 하위 모듈이 존재할 때, <b>단일 모듈</b>을 요구하는 의존성에서 기본으로 선택될 모듈을 말합니다.
         * 이는 {@link org.smoodi.core.annotation.Module @Module} 어노테이션의 <code>isPrimary()</code> 매개변수로 설정됩니다.
         * <p>
         * 예를 들어, List 인터페이스 타입에 여러 개의 구현, ArrayList, SortedList, UnmodifiableList가 있다고 하면,<br>
         * 이 때 일반적으로 사용할 기본 구현을 하나 지정합니다. 마치 List.of()가 내부적으로 UnmodifiableList를 반환하는 것과 유사합니다.<br>
         * 이렇게 지정된 기본 모듈은, List 타입을 요구하는 의존성에 주입될 때 자동으로 선택됩니다.
         * <p>
         * 기본 모듈은 반드시 단일 모듈이어야 하며, 다수의 기본 모듈이 존재하거나, 기본 모듈이 아예 없는 경우에는
         * {@link org.smoodi.core.module.ModuleDeclareError 모듈 선언 오류}가 발생합니다.
         * <p>
         * 기본 모듈은 반드시 인스턴스화 가능한 타입이어야 하며, 인터페이스나 추상 클래스는 될 수 없습니다.
         * <p>
         * 기본 모듈은 프록시 객체, 컴포지트 패턴 등 원본 대신 제공되는 객체를 지정할 때도 유용합니다.
         *
         * @see org.smoodi.core.annotation.Module#isPrimary()
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class PrimaryModuleDocs {
        }

        /**
         * 하나의 타입에 다수의 모듈이 등록될 수 있으며, IoC 컨테이너는 이를 <code>Collection</code>으로 제공합니다.
         * 이때 {@link AnnotationDocs @Module}의 <code>order()</code>로 정렬 우선순위를 지정할 수 있습니다.
         * <p>
         * 기본적으로 순서는 없으며, 순서를 지정하지 않은 경우에는 무작위 배치가 발생할 수 있습니다.<br>
         * 동일한 순서 값끼리는 내부적으로 무작위 섞기(shuffle)를 수행하므로, 필요한 경우 명시적으로 순서를 지정하는 것을 권장합니다.<br>
         * 이는 클래스 이름 변경이나 컴파일러 버전 변경 등 개발자의 인지 하에 있지 않은 외부 요인에 의한 오작동을 방지하기 위함입니다.
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class OrderedModulesDocs {
        }

        /**
         * 정적 모듈<sup>Static Module</sup>이란, 모듈 로더에 의해 인스턴스화가 진행되지 않지만 IoC 컨테이너에 등록할 이유가 있어 <b>수동</b>으로 직접 등록하는 모듈입니다.
         * <p>
         * 일반 사용자가 정의할 일은 드물며, 프레임워크 자체의 일부 구성요소(예: IoC 컨테이너 자신, 프레임워크 생명주기 등)가 여기에 해당합니다.
         * <p>
         * 자동 인스턴스화를 피해야 하는 경우 {@link org.smoodi.core.annotation.Module#IoC @Module의 IoC() 옵션}을 false로 설정하여 자동 등록 대상에서 제외합니다.<br>
         * {@link org.smoodi.core.annotation.StaticModule @StaticModule} 어노테이션 또한 위처럼 구현되어 있습니다.
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class StaticModuleDocs {
        }

        /**
         * 모듈 로더<sup>Module Loader</sup>란, 클래스패스 상의 모든 클래스를 스캔하여 {@link org.smoodi.core.annotation.Module @Module} 어노테이션이 부착된 타입을 찾아내고,
         * 이를 인스턴스화하여 {@link IocContainerDocs IoC 컨테이너}에 등록하는 역할을 담당합니다.
         * <p>
         * 모듈을 스캔하고 어노테이션 메타데이터를 해석하여 의존성 그래프를 구성한 뒤, 생성 순서를 계산하고 인스턴스를 생성/등록합니다.<br>
         * 이 과정에서 의존성 주입이 자동으로 이루어지며, 순환 참조가 있는 경우에는 {@link org.smoodi.core.module.ModuleDeclareError 순환 참조 오류}가 발생합니다.
         * <p>
         * Smoodi에서는 모듈 로더의 책임을 넓은 의미의 IoC 컨테이너 범주에 포함하여 설명합니다.
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class ModuleLoaderDocs {
        }
    }

    /**
     * IoC 컨테이너<sup>IoC Container</sup>란, 제어의 역전<sup>Inversion of Control</sup>을 구현하며, 그러한 객체들을 보관 및 관리하기 위한 저장소입니다.
     * <p>
     * Project Smoodi에서 IoC는 {@link ObjectLifecycleDocs 객체 생명주기}가 관리되며, 동시에 의존성의 주입과 같은 의존성 관리까지를 포함한 개념입니다.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class IocContainerDocs {

        /**
         * 객체 생명주기<sup>Object Lifecycle</sup>란, 객체의 생성<sup>Create</sup>부터 파괴<sup>Destroy</sup>까지의 과정을 의미합니다.
         * <p>
         * Smoodi에서는 JVM의 생명주기를 Create, Use, No Refer, Destroy의 네 단계로 구분합니다.
         * <p>
         * <ol>
         *     <li>
         *         <b>Create</b>: 객체의 생성이란, 보다 정확히는 인스턴스화<sup>instantiate</sup>를 의미합니다.<br>
         *         생성자가 호출되어, Heap 메모리에 객체가 등록되는 과정입니다.
         *     </li>
         *     <li>
         *         <b>Use</b>: 단어 그대로, 사용되는 동안의 단계입니다. 이러한 단계는 No Refer와 명확히 구분됩니다.
         *     </li>
         *     <li>
         *         <b>No Refer</b>: 객체의 본 목적을 수행한 후, 더이상의 쓸모가 없는 객체의 단계입니다.<br>
         *         JVM 상에서 이 객체에 대한 참조가 없는 시기입니다. 이 이후에 GC<sup>Garbage Collector</sup>에 의해 Destroy 단계를 거칩니다.
         *     </li>
         *     <li>
         *         <b>Destroy</b>: 메모리 해제를 의미합니다.<br>
         *         실제로 메모리가 해제되는, GC<sup>Garbage Collector</sup>에 의해 수집<sup>Collect</sup>되는 단계를 말합니다.<br>
         *     </li>
         * </ol>
         * 여기에서 Smoodi가 관리하는 생명주기 단계는 Create, Use, No Refer입니다.<br>
         * 또한, 다른 곳에서 말하는 객체 생명주기란, 따로 언급하지 않는 한 Smoodi가 관리하는 세 단계로 이루어진 객체 생명주기를 말합니다.<br>
         * 참고로, Destroy가 제외된 이유는 JVM과 GC를 직접 제어해야 하기 때문입니다. 이러한 과정은 복잡하고, 유연하지 못하며, 무엇보다 JVM의 메모리 관리 시스템이 더 안정적이고 신뢰성이 높기 때문입니다.
         */
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class ObjectLifecycleDocs {
        }
    }
}
