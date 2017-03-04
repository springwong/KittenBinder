package com.spring.kittenbinder.binding;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.spring.kittenbinder.R;

/**
 * Created by spring on 4/3/2017.
 */

final class KittenViewBind {
    static void setBackground(View view , TypedArray ta){
        if(ta.hasValue(R.styleable.KittenView_android_background)){
            Drawable drawable = ta.getDrawable(R.styleable.KittenView_android_background);
            if(drawable != null){
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackgroundDrawable(drawable);
                } else {
                    view.setBackground(drawable);
                }
            }else {
                final int backgroundColor = ta.getColor(R.styleable.KittenView_android_background, 0);
                view.setBackgroundColor(backgroundColor);
            }
        }

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
        final int visibility = ta.getInt(R.styleable.KittenView_android_visibility, -1);
        if (visibility != -1){
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
