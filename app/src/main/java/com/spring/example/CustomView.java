package com.spring.example;

import android.content.Context;
import android.util.AttributeSet;

import android.widget.LinearLayout;
import android.widget.TextView;

import kittenbinder.KittenBind;

import kittenbinder.BindContext;

/**
 * TODO: document your custom view class.
 */
public class CustomView extends LinearLayout {
    @BindContext
    TextView textView;

    public CustomView(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        KittenBind.bind(this);
        setOrientation(VERTICAL);
        addView(textView);
    }
}
