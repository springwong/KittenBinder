package kittenbinder;

import android.support.annotation.BoolRes;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by spring on 5/3/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindImageView {
    @DrawableRes int value() default -1;
    boolean adjustViewBounds() default false;
    ImageView.ScaleType scaleType() default ImageView.ScaleType.FIT_CENTER;

}
