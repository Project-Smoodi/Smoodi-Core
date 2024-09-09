package org.smoodi.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Module
public @interface SmoodiDefaultConfiguration {
}
