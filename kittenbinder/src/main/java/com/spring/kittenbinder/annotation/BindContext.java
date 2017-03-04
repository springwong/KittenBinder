package com.spring.kittenbinder.annotation;

/**
 * Created by spring on 28/2/2017.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindContext {
    int style() default 0;
    int background() default -1;
}