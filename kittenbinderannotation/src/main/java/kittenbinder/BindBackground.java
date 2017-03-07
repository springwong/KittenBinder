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

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindBackground {
    @ColorRes @DrawableRes int value();
}
