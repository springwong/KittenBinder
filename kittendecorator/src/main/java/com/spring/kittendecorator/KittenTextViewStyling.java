package com.spring.kittendecorator;

import android.content.res.TypedArray;
import android.text.TextUtils;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * Created by spring on 4/3/2017.
 */

final class KittenTextViewStyling {
    static void setTextViewStyle(TextView textView, TypedArray a){
//        setGravity(textView, a);
        textView.setHint(a.getText(R.styleable.KittenView_android_hint));
        textView.setText(a.getText(R.styleable.KittenView_android_text));
        textView.setSingleLine(a.getBoolean(R.styleable.KittenView_android_singleLine, false));
        if(a.hasValue(R.styleable.KittenView_android_ellipsize)){
            int ellipsize = a.getInt(R.styleable.KittenView_android_ellipsize, 1);
            switch (ellipsize) {
                case 1:
                    textView.setEllipsize(TextUtils.TruncateAt.START);
                    break;
                case 2:
                    textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                    break;
                case 3:
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    break;
                case 4:
                    textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    break;
            }
        }

        textView.setMinLines(a.getInt(R.styleable.KittenView_android_minLines, -1));
        textView.setMaxLines(a.getInt(R.styleable.KittenView_android_maxLines, -1));
        textView.setLines(a.getInt(R.styleable.KittenView_android_lines, -1));
        textView.setGravity(a.getInt(R.styleable.KittenView_android_gravity, -1));
    }
//    public static void setGravity(TextView textView, TypedArray ta){
//        final int gravity = ta.getInt(R.styleable.KittenView_android_gravity, -1);
//        if (gravity != -1 ) {
//            textView.setGravity(gravity);
//        }
//    }
}
