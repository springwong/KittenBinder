package com.spring.kittendecorator;

import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by spring on 4/3/2017.
 */

final class KittenImageViewStyling {
    private static final ImageView.ScaleType[] sScaleTypeArray = {
            ImageView.ScaleType.MATRIX,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.FIT_START,
            ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_END,
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.CENTER_INSIDE
    };

    static void setImageViewStyle(ImageView view, TypedArray a){
        final Drawable d = a.getDrawable(R.styleable.KittenView_android_src);
        if (d != null) {
            view.setImageDrawable(d);
        }

        view.setAdjustViewBounds(a.getBoolean(R.styleable.KittenView_android_adjustViewBounds, false));

        final int index = a.getInt(R.styleable.KittenView_android_scaleType, -1);
        if (index >= 0) {
            view.setScaleType(sScaleTypeArray[index]);
        }

        final int alpha = a.getInt(R.styleable.KittenView_android_drawableAlpha, 255);
        if (alpha != 255) {
            view.setImageAlpha(alpha);
        }

        view.setCropToPadding(a.getBoolean(
                R.styleable.KittenView_android_cropToPadding, false));
    }
}
