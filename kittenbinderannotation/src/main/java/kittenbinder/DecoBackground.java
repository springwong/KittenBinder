package kittenbinder;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by spring on 5/3/2017.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface DecoBackground {
    @ColorRes @DrawableRes int value() default -1;
}
