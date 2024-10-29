package org.smoodi.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Module(IoC = true, isPrimary = false, order = Byte.MAX_VALUE)
public @interface SmoodiDefaultConfiguration {
}
