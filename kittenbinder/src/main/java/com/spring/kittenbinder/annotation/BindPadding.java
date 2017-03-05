package com.spring.kittenbinder.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by spring on 5/3/2017.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindPadding {
    int value();
    int left() default Integer.MIN_VALUE;
    int top() default Integer.MIN_VALUE;
    int right() default Integer.MIN_VALUE;
    int bottom() default Integer.MIN_VALUE;
}
