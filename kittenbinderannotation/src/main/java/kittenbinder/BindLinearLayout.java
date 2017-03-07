package kittenbinder;

import android.support.annotation.IntDef;
import android.widget.LinearLayout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by spring on 5/3/2017.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindLinearLayout {
    int value() default LinearLayout.VERTICAL;
}
