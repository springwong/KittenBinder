package kittenbinder;

/**
 * Created by spring on 5/3/2017.
 */

import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindVisibility {
    int value() default View.VISIBLE;
}
