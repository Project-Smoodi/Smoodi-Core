package com.smoodi.module.util;

import org.smoodi.core.annotation.Module;
import org.smoodi.core.annotation.ModuleInitConstructor;
import org.smoodi.core.util.ModuleUtilsTest;

public class ModuleInitConstructorFinderCases {

    private ModuleInitConstructorFinderCases() {
    }

    /**
     * <p>1번 케이스, 단일 기본 생성자를 가진 경우</p>
     */
    @Module
    public static class Case1 {

        @ModuleUtilsTest.ModuleInitConstructorTest.TargetConstructor
        public Case1() {
        }
    }

    /**
     * <p>2번 케이스, 단일 생성자를 가진 경우/p>
     */
    @Module
    public static class Case2 {

        @ModuleUtilsTest.ModuleInitConstructorTest.TargetConstructor
        public Case2(
                Integer param
        ) {
        }
    }

    /**
     * <p>3번 케이스, 단일 기본 생성자와 임의의 단일 생성자 가진 경우</p>
     */
    @Module
    public static class Case3 {

        @ModuleUtilsTest.ModuleInitConstructorTest.TargetConstructor
        public Case3() {
        }

        public Case3(Integer param) {
        }
    }

    /**
     * <p>4번 케이스, 기본 생성자와 임의의 생성자를 다수 가진 경우</p>
     */
    @Module
    public static class Case4 {

        @ModuleUtilsTest.ModuleInitConstructorTest.TargetConstructor
        public Case4() {
        }

        public Case4(Integer param) {
        }

        public Case4(Double param) {
        }
    }

    /**
     * <p>5번 케이스, 생성자가 지정되었으며 단일 생성자를 가진 경우</p>
     */
    @Module
    public static class Case5 {

        @ModuleUtilsTest.ModuleInitConstructorTest.TargetConstructor
        @ModuleInitConstructor
        public Case5() {
        }
    }

    /**
     * <p>6번 케이스, 생성자가 지정되었으며 생성자를 다수 가진 경우</p>
     */
    @Module
    public static class Case6 {

        public Case6() {
        }

        @ModuleUtilsTest.ModuleInitConstructorTest.TargetConstructor
        @ModuleInitConstructor
        public Case6(Integer param) {
        }
    }
}
