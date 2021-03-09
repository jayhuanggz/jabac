package com.essue.jabac.client.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceEntity {

  String namespace();

  ResourceAttribute[] attributes() default {};
}
