package com.spring.kittendecorator;

import android.annotation.TargetApi;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.lang.reflect.Type;

/**
 * Created by spring on 4/3/2017.
 */

final class KittenViewStyling {
    static void setViewStyle(View view , TypedArray ta){
        setBackground(view, ta);
        setVisibility(view, ta);
        setPadding(view, ta);
        setAlpha(view, ta);
        setClickable(view, ta);
    }

    @SuppressWarnings("deprecation")
    static void setBackground(View view , TypedArray ta){
        if(ta.hasValue(R.styleable.KittenView_android_background)){
            Drawable drawable = ta.getDrawable(R.styleable.KittenView_android_background);
            if(drawable != null){
                int sdk = Build.VERSION.SDK_INT;
                if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackgroundDrawable(drawable);
                } else {
                    setBackgroundDrawable(view, drawable);
                }
            }else {
                final int backgroundColor = ta.getColor(R.styleable.KittenView_android_background, 0);
                view.setBackgroundColor(backgroundColor);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void setBackgroundDrawable(View view, Drawable drawable){
        view.setBackground(drawable);
    }
    static void setVisibility(View view, TypedArray ta){
        if(ta.hasValue(R.styleable.KittenView_android_visibility)){
            final int visibility = ta.getInt(R.styleable.KittenView_android_visibility, -1);
            switch (visibility){
                case 0:
                    view.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    view.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    view.setVisibility(View.GONE);
                    break;
            }
        }
    }
    static void setPadding(View view, TypedArray ta){
        int paddingTop = 0;
        int paddingRight = 0;
        int paddingBottom = 0;
        int paddingLeft = 0;
        if(ta.hasValue(R.styleable.KittenView_android_padding)){
            int value = ta.getDimensionPixelOffset(R.styleable.KittenView_android_padding, 0);
            paddingTop = value;
            paddingBottom = value;
            paddingRight = value;
            paddingLeft = value;
        }

        if(ta.hasValue(R.styleable.KittenView_android_paddingLeft)){
            int value = ta.getDimensionPixelOffset(R.styleable.KittenView_android_paddingLeft, 0);
            paddingLeft = value;
        }
        if(ta.hasValue(R.styleable.KittenView_android_paddingRight)){
            int value = ta.getDimensionPixelOffset(R.styleable.KittenView_android_paddingRight, 0);
            paddingRight = value;
        }
        if(ta.hasValue(R.styleable.KittenView_android_paddingTop)){
            int value = ta.getDimensionPixelOffset(R.styleable.KittenView_android_paddingTop, 0);
            paddingTop = value;
        }
        if(ta.hasValue(R.styleable.KittenView_android_paddingBottom)){
            int value = ta.getDimensionPixelOffset(R.styleable.KittenView_android_paddingBottom, 0);
            paddingBottom = value;
        }
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }
    static void setAlpha(View view , TypedArray ta){
        if(ta.hasValue(R.styleable.KittenView_android_alpha)) {
            float alpha = ta.getFloat(R.styleable.KittenView_android_alpha, -1);
            view.setAlpha(alpha);
        }
    }
    static void setClickable(View view, TypedArray ta){
        if (ta.hasValue(R.styleable.KittenView_android_clickable)){
            boolean bool = ta.getBoolean(R.styleable.KittenView_android_clickable, false);
            view.setClickable(bool);
        }
    }
}
