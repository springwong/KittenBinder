package com.spring.kittenbinder.annotation;

import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;
import android.support.annotation.IntDef;
import android.support.annotation.Size;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by spring on 5/3/2017.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindTextView {
    @StringRes int value();
    @DimenRes int textSize();

    int gravity() default Gravity.LEFT;
//    int textAlignment();
    int minLines() default -1;
    int maxLines() default -1;
    int lines() default -1;


}
