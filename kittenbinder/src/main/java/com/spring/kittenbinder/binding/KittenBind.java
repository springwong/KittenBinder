package com.spring.kittenbinder.binding;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spring.kittenbinder.R;
import com.spring.kittenbinder.annotation.BindContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by spring on 28/2/2017.
 */

public class KittenBind {
    public static void bind(Activity activity){
        bind(activity, activity.getApplicationContext());
    }
    public static void bind(Fragment fragment){
        bind(fragment, fragment.getActivity());
    }
    public static void bind(View view){
        bind(view, view.getContext());
    }
    public static void bind(android.support.v4.app.Fragment fragment){
        bind(fragment, fragment.getActivity());
    }
    public static void bind(Object object, Context context) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            BindContext bindContext = field.getAnnotation(BindContext.class);
            if (bindContext != null) {
                Class tClass = field.getType();
                if (View.class.isAssignableFrom(tClass)) {
                    try {
                        Constructor constructor = tClass.getConstructor(Context.class);
                        field.set(object, constructor.newInstance(context));

                        //it work
//                        int background = bindContext.background();
//                        View view = (View)field.get(object);
//                        int backgroundRes = bindContext.background();
//                        if(backgroundRes != -1){
//                            int color = context.getResources().getColor(backgroundRes);
//                            view.setBackgroundColor(color);
//                        }

                        int style = bindContext.style();
                        setStyle((View)field.get(object), style, context);
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (InstantiationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static void setStyle(View view, int styleResId, Context context) {
        final TypedArray ta = context.obtainStyledAttributes(styleResId, R.styleable.KittenView);
        setBackground(view, ta);
        setVisibility(view, ta);
        setPadding(view, ta);
        if (view instanceof TextView){
            setGravity((TextView)view, ta);
        }
        if (view instanceof ImageView){
            setSrc((ImageView)view, ta);
        }

        ta.recycle();
    }
    private static void setSrc(ImageView view, TypedArray ta){
        Drawable drawable = ta.getDrawable(R.styleable.KittenView_android_src);
        if(drawable != null){
            view.setImageDrawable(drawable);
        }
    }
    private static void setBackground(View view , TypedArray ta){
        final int backgroundColor = ta.getColor(R.styleable.KittenView_android_background, 0);
        if (backgroundColor != -1) {
            view.setBackgroundColor(backgroundColor);
        }else{
            Drawable drawable = ta.getDrawable(R.styleable.KittenView_android_background);
            if(drawable != null){
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackgroundDrawable(drawable);
                } else {
                    view.setBackground(drawable);
                }

            }
        }
    }
    private static void setGravity(TextView textView, TypedArray ta){
        final int gravity = ta.getInt(R.styleable.KittenView_android_gravity, -1);
        if (gravity != -1 ) {
            textView.setGravity(gravity);
        }
    }
    private static void setVisibility(View view, TypedArray ta){
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
    private static void setPadding(View view, TypedArray ta){
        int paddingTop = 0;
        int paddingRight = 0;
        int paddingBottom = 0;
        int paddingLeft = 0;
        int value = ta.getDimensionPixelOffset(R.styleable.KittenView_android_padding, 0);
        if (value != 0){
            paddingTop = value;
            paddingBottom = value;
            paddingRight = value;
            paddingLeft = value;
        }
        value = ta.getDimensionPixelOffset(R.styleable.KittenView_android_paddingLeft, 0);
        if(value != 0){
            paddingLeft = value;
        }
        value = ta.getDimensionPixelOffset(R.styleable.KittenView_android_paddingRight, 0);
        if(value != 0){
            paddingRight = value;
        }
        value = ta.getDimensionPixelOffset(R.styleable.KittenView_android_paddingTop, 0);
        if(value != 0){
            paddingTop = value;
        }
        value = ta.getDimensionPixelOffset(R.styleable.KittenView_android_paddingBottom, 0);
        if(value != 0){
            paddingBottom = value;
        }
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

}
