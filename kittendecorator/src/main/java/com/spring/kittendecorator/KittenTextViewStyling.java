package com.spring.kittendecorator;

import android.content.res.TypedArray;
import android.widget.TextView;

/**
 * Created by spring on 4/3/2017.
 */

final class KittenTextViewStyling {
    public static void setGravity(TextView textView, TypedArray ta){
        final int gravity = ta.getInt(R.styleable.KittenView_android_gravity, -1);
        if (gravity != -1 ) {
            textView.setGravity(gravity);
        }
    }
}
