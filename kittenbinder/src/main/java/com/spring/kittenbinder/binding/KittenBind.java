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

import static com.spring.kittenbinder.binding.KittenImageViewBind.setSrc;
import static com.spring.kittenbinder.binding.KittenTextViewBind.setGravity;

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
        KittenViewBind.setBackground(view, ta);
        KittenViewBind.setVisibility(view, ta);
        KittenViewBind.setPadding(view, ta);
        if (view instanceof TextView){
            setGravity((TextView)view, ta);
        }
        if (view instanceof ImageView){
            setSrc((ImageView)view, ta);
        }

        ta.recycle();
    }

}
