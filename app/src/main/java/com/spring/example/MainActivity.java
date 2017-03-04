package com.spring.example;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spring.kittenbinder.annotation.BindContext;
import com.spring.kittenbinder.binding.KittenBind;

public class MainActivity extends AppCompatActivity {

    @BindContext
    LinearLayout mainView;
    @BindContext
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KittenBind.bind(this);
        setContentView(mainView);

        mainView.addView(textView);
        textView.setText("Testing text");
        textView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }
}
