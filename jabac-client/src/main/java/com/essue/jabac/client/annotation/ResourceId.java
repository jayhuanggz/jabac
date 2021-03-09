package com.essue.jabac.client.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceId {

  String name() default "";

  String alias() default "";
}
