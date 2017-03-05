package com.spring.example;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spring.kittenbinder.annotation.BindBackground;
import com.spring.kittenbinder.annotation.BindContext;
import com.spring.kittenbinder.annotation.BindLinearLayout;
import com.spring.kittenbinder.annotation.BindTextView;
import com.spring.kittenbinder.annotation.BindVisibility;
import com.spring.kittenbinder.binding.KittenBind;

public class MainActivity extends AppCompatActivity {

    @BindContext @BindLinearLayout
    LinearLayout mainView;

    @BindContext @BindBackground(android.R.color.holo_red_dark)
    TextView textView;

    @BindContext
    @BindBackground(android.R.color.holo_green_light)
    @BindTextView(value = R.string.sample1, textSize = R.dimen.text_sample_size, gravity = Gravity.CENTER_HORIZONTAL)
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KittenBind.bind(this);
        setContentView(mainView);

        mainView.addView(textView);
        mainView.addView(textView2);

        textView.setText("Testing text");
    }
}
