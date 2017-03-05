package com.spring.example;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spring.kittenbinder.annotation.BindBackground;
import com.spring.kittenbinder.annotation.BindContext;
import com.spring.kittenbinder.annotation.BindVisibility;
import com.spring.kittenbinder.binding.KittenBind;

public class MainActivity extends AppCompatActivity {

    @BindContext
    LinearLayout mainView;
    @BindContext
    @BindBackground(android.R.color.holo_red_dark)
    @BindVisibility(View.VISIBLE)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KittenBind.bind(this);
        setContentView(mainView);
        mainView.addView(textView);
        textView.setText("Testing text");
    }
}
