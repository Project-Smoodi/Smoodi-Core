package org.smoodi.core.util;

import com.smoodi.module.util.ModuleInitConstructorFinderCases;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smoodi.core.TestBase;
import org.smoodi.core.module.ModuleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;

@Tag("fork")
public class ModuleUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(ModuleUtilsTest.class);

    @Tag("fork")
    @Nested
    public class ModuleInitConstructorTest {

        @Target({ElementType.CONSTRUCTOR})
        @Retention(RetentionPolicy.RUNTIME)
        public @interface TargetConstructor {
        }

        void doTest(Class<?> klass) {
            TestBase.initWith(ModuleUtilsTest.class);

            log.info("{}", ModuleUtils.findModuleInitConstructor(ModuleType.of(klass)));

            Assertions.assertTrue(
                    isTargetConstructor(
                            ModuleUtils.findModuleInitConstructor(ModuleType.of(klass))
                    )
            );
        }

        boolean isTargetConstructor(Constructor<?> constructor) {
            return AnnotationUtils.findAnnotation(constructor, TargetConstructor.class) != null;
        }

        @Test
        public void case1() {
            doTest(ModuleInitConstructorFinderCases.Case1.class);
        }

        @Test
        public void case2() {
            doTest(ModuleInitConstructorFinderCases.Case2.class);
        }

        @Test
        public void case3() {
            doTest(ModuleInitConstructorFinderCases.Case3.class);
        }

        @Test
        public void case4() {
            doTest(ModuleInitConstructorFinderCases.Case4.class);
        }

        @Test
        public void case5() {
            doTest(ModuleInitConstructorFinderCases.Case5.class);
        }

        @Test
        public void case6() {
            doTest(ModuleInitConstructorFinderCases.Case6.class);
        }
    }
}
