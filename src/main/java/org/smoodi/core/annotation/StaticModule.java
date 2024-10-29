package org.smoodi.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Module(IoC = false, order = Byte.MAX_VALUE)
public @interface StaticModule {
}
