package com.spring.kittenbinder.binding;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spring.kittenbinder.R;
import com.spring.kittenbinder.annotation.BindBackground;
import com.spring.kittenbinder.annotation.BindContext;
import com.spring.kittenbinder.annotation.BindEditText;
import com.spring.kittenbinder.annotation.BindImageView;
import com.spring.kittenbinder.annotation.BindPadding;
import com.spring.kittenbinder.annotation.BindStyle;
import com.spring.kittenbinder.annotation.BindTextAppearance;
import com.spring.kittenbinder.annotation.BindTextView;
import com.spring.kittenbinder.annotation.BindVisibility;

import org.w3c.dom.Text;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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
    public static void bind(Object object, Context context) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            View view = bindContext(object, field, context);;
            bindStyle(view, field, context);
            bindPadding(view, field, context);
            bindVisibility(view, field, context);
            bindBackground(view ,field, context);
            bindImageView(view, field, context);
            bindTextAppearance(view, field, context);
            bindTextView(view, field, context);
            bindEditText(view, field, context);
        }
    }

    public static View bindContext(Object object, Field field, Context context){
        BindContext bindContext = field.getAnnotation(BindContext.class);
        if (bindContext != null) {
            Class tClass = field.getType();
            if (View.class.isAssignableFrom(tClass)) {
                try {
                    Constructor constructor = tClass.getConstructor(Context.class);
                    field.set(object, constructor.newInstance(context));
                    return (View)field.get(object);
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
        return null;
    }
    public static void bindEditText(View view, Field field, Context context) {
        if(view instanceof TextView){
            TextView textView = (TextView)view;
            BindEditText bind = field.getAnnotation(BindEditText.class);
            if(bind!=null){
                textView.setSingleLine(bind.isSingleLine());
                textView.setHint(bind.hint());
            }
        }

    }
    public static void bindBackground(View view, Field field, Context context) {
        BindBackground bind = field.getAnnotation(BindBackground.class);
        if(bind!=null){
            view.setBackgroundResource(bind.value());
        }
    }
    public static void bindVisibility(View view, Field field, Context context){
        BindVisibility bind = field.getAnnotation(BindVisibility.class);
        if(bind!=null){
            switch (bind.value()) {
                case View.VISIBLE:
                    view.setVisibility(View.VISIBLE);
                    break;
                case View.INVISIBLE:
                    view.setVisibility(View.INVISIBLE);
                    break;
                case View.GONE:
                    view.setVisibility(View.GONE);
                    break;
            }
        }
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
    public static void bindPadding(View view, Field field, Context context){
        BindPadding bind = field.getAnnotation(BindPadding.class);
        if(bind!=null){
            int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bind.padding(), context.getResources().getDisplayMetrics());
            view.setPadding(padding, padding, padding, padding);
        }

    }
    public static void bindTextView(View view, Field field, Context context){
        if(view instanceof TextView){
            BindTextView bind = field.getAnnotation(BindTextView.class);
            if(bind!=null){
                TextView textView = (TextView) view;
                textView.setText(bind.value());
                float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, bind.textSize(), context.getResources().getDisplayMetrics());
                textView.setTextSize(textSize);
                if(bind.minLines() != -1){
                    textView.setMinLines(bind.minLines());
                }
                if(bind.maxLines() != -1){
                    textView.setMaxLines(bind.maxLines());
                }
                if(bind.lines() != -1){
                    textView.setLines(bind.lines());
                }
            }

        }
    }
    public static void bindImageView(View view, Field field, Context context){
        if(view instanceof ImageView){
            BindImageView bind = field.getAnnotation(BindImageView.class);
            if(bind!=null){
                ImageView imageView = (ImageView)view;
                imageView.setImageResource(bind.value());
                imageView.setAdjustViewBounds(bind.adjustViewBounds());
                imageView.setScaleType(bind.scaleType());
            }
        }

    }
    public static void bindStyle(View view, Field field, Context context){
        BindStyle style = field.getAnnotation(BindStyle.class);
        if(style!=null){
            setStyle(view, style.value(), context);
        }
    }

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

}
