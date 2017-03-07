package com.spring.kittenbinder.binding;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spring.kittenbinder.R;
import kittenbinder.BindBackground;
import kittenbinder.BindContext;
import kittenbinder.BindEditText;
import kittenbinder.BindImageView;
import kittenbinder.BindLinearLayout;
import kittenbinder.BindPadding;
import kittenbinder.BindStyle;
import kittenbinder.BindTextAppearance;
import kittenbinder.BindTextView;
import kittenbinder.BindVisibility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.spring.kittenbinder.binding.KittenImageViewStyling.setSrc;
import static com.spring.kittenbinder.binding.KittenTextViewStyling.setGravity;

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

    public static void bind(Object target, Context context) {
        createBinding(target, context);
//        Field[] fields = object.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            View view = bindContext(object, field, context);;
//            bindStyle(view, field, context);
//            bindPadding(view, field, context);
//            bindVisibility(view, field, context);
//            bindBackground(view ,field, context);
//            bindImageView(view, field, context);
//            bindTextAppearance(view, field, context);
//            bindTextView(view, field, context);
//            bindEditText(view, field, context);
//            bindLinearLayout(view, field, context);
//        }
    }

    @SuppressWarnings("deprecation")
    public static void bindTextAppearance(View view, Field field, Context context){
        if(view instanceof TextView){
            BindTextAppearance bind = field.getAnnotation(BindTextAppearance.class);
            if(bind!=null){
                TextView textView = (TextView) view;
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.M) {
                    setTextAppearance(textView, bind.value());
                } else {
                    textView.setTextAppearance(context, bind.value());
                }
            }

        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    public static void setTextAppearance(TextView textView, int resId){
        textView.setTextAppearance(resId);
    }

    public static void bindStyle(View view, Field field, Context context){
        BindStyle style = field.getAnnotation(BindStyle.class);
        if(style!=null){
            setStyle(view, style.value(), context);
        }
    }

    //todo : long way to fill all fields assignment
    public static void setStyle(View view, int styleResId, Context context) {
        final TypedArray ta = context.obtainStyledAttributes(styleResId, R.styleable.KittenView);
        KittenViewStyling.setBackground(view, ta);
        KittenViewStyling.setVisibility(view, ta);
        KittenViewStyling.setPadding(view, ta);
        if (view instanceof TextView){
            setGravity((TextView)view, ta);
        }
        if (view instanceof ImageView){
            setSrc((ImageView)view, ta);
        }
        ta.recycle();
    }

    private static void createBinding(@NonNull Object target, @NonNull Context context) {
        Class<?> targetClass = target.getClass();
        try {
            Class<?> classZ = Class.forName(target.getClass().getName() + "_ViewDecorator");
            Object decorator = classZ.newInstance();
            Method bindMethod = classZ.getDeclaredMethod("bind", targetClass, Context.class);
            bindMethod.invoke(decorator, target, context);
        }catch (ClassNotFoundException exception){
            exception.printStackTrace();
        }catch (IllegalAccessException ex){
            ex.printStackTrace();
        }catch (InstantiationException ex){
            ex.printStackTrace();
        }catch (NoSuchMethodException ex){
            ex.printStackTrace();
        }catch (InvocationTargetException ex){
            ex.printStackTrace();
        }
    }

}
