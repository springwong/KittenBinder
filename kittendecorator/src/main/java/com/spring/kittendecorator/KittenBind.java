package com.spring.kittendecorator;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by spring on 28/2/2017.
 */

public class KittenBind {
    public static void bind(Activity activity){
        bind(activity, activity.getApplicationContext());
    }
//    public static void bind(Fragment fragment){
//        bind(fragment, fragment.getActivity());
//    }
    public static void bind(View view){
        bind(view, view.getContext());
    }
//    public static void bind(android.support.v4.app.Fragment fragment){
//        bind(fragment, fragment.getActivity());
//    }

    public static void bind(Object target, Context context) {
        createBinding(target, context);
    }

//    @SuppressWarnings("deprecation")
//    public static void bindTextAppearance(View view, Field field, Context context){
//        if(view instanceof TextView){
//            DecoTextAppearance bind = field.getAnnotation(DecoTextAppearance.class);
//            if(bind!=null){
//                TextView textView = (TextView) view;
//                int sdk = android.os.Build.VERSION.SDK_INT;
//                if(sdk < android.os.Build.VERSION_CODES.M) {
//                    setTextAppearance(textView, bind.value());
//                } else {
//                    textView.setTextAppearance(context, bind.value());
//                }
//            }
//
//        }
//    }
//    @TargetApi(Build.VERSION_CODES.M)
//    public static void setTextAppearance(TextView textView, int resId){
//        textView.setTextAppearance(resId);
//    }

    //todo : long way to fill all fields assignment
    public static void setStyle(View view, int styleResId, Context context) {
        final TypedArray ta = context.obtainStyledAttributes(styleResId, R.styleable.KittenView);
        KittenViewStyling.setBackground(view, ta);
        KittenViewStyling.setVisibility(view, ta);
        KittenViewStyling.setPadding(view, ta);
        if (view instanceof TextView){
            KittenTextViewStyling.setGravity((TextView)view, ta);
        }
        if (view instanceof ImageView){
            KittenImageViewStyling.setSrc((ImageView)view, ta);
        }
        ta.recycle();
    }

    private static void createBinding(Object target, Context context) {
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
