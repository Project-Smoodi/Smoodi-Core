package org.smoodi.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModuleInitConstructor {
}
