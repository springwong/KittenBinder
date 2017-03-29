package com.spring.kittendecorator;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by spring on 4/3/2017.
 */

final class KittenImageViewStyling {
    public static void setSrc(ImageView view, TypedArray ta){
        Drawable drawable = ta.getDrawable(R.styleable.KittenView_android_src);
        if(drawable != null){
            view.setImageDrawable(drawable);
        }
    }
}
