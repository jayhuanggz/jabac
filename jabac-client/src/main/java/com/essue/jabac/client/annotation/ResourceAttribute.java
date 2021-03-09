package com.essue.jabac.client.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceAttribute {

  String name() default "";

  String alias() default "";

  String path() default "";

}
