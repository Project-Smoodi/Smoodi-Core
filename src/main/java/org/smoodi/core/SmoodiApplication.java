package org.smoodi.core;

import org.smoodi.core.module.ModuleInitConstructorTargeter;
import org.smoodi.core.module.TargetBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ModuleInitConstructorTargeter(target = TargetBy.DEFAULT)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SmoodiApplication {
}
