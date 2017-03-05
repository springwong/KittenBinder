package com.spring.example;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spring.kittenbinder.annotation.BindBackground;
import com.spring.kittenbinder.annotation.BindContext;
import com.spring.kittenbinder.annotation.BindEditText;
import com.spring.kittenbinder.annotation.BindImageView;
import com.spring.kittenbinder.annotation.BindLinearLayout;
import com.spring.kittenbinder.annotation.BindPadding;
import com.spring.kittenbinder.annotation.BindTextAppearance;
import com.spring.kittenbinder.annotation.BindTextView;
import com.spring.kittenbinder.annotation.BindVisibility;
import com.spring.kittenbinder.binding.KittenBind;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @BindContext @BindLinearLayout
    LinearLayout mainView;

    @BindContext
    @BindPadding(10)
    @BindBackground(android.R.color.holo_red_dark)
    TextView textView;

    @BindContext
    @BindBackground(android.R.color.holo_green_light)
    @BindTextView(value = R.string.sample1, textSize = R.dimen.text_sample_size, gravity = Gravity.CENTER_HORIZONTAL)
    TextView textView2;

    @BindContext
    @BindImageView(android.R.drawable.btn_default)
    ImageView imageView;

    @BindContext
    @BindEditText(hint = R.string.sample1)
    EditText editText;

    @BindContext
    @BindTextView(R.string.sample1)
    @BindTextAppearance(R.style.sample_text_style)
    TextView textViewC;

    //BindTextView override some seting of textAppearance
    @BindContext
    @BindTextView(value = R.string.sample1, textSize = R.dimen.text_sample_size, textColor = R.color.colorPrimaryDark)
    @BindTextAppearance(R.style.sample_text_style)
    TextView textViewD;

    @BindContext
    @BindBackground(android.R.drawable.btn_default)
    @BindTextView(value = R.string.sample1, gravity = Gravity.CENTER, textColor = R.color.colorPrimaryDark)
    @BindPadding(10)
    TextView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long benchmarkStart = System.currentTimeMillis();
        View benchmark = LayoutInflater.from(this).inflate(R.layout.benchmark, null, false);
        Log.d("KittenBinder", "" + (System.currentTimeMillis() - benchmarkStart));

        long start = System.currentTimeMillis();
        KittenBind.bind(this);
        Log.d("KittenBinder", "" + (System.currentTimeMillis() - start));
        setContentView(mainView);

        mainView.setOrientation(LinearLayout.VERTICAL);
        mainView.addView(textView);
        mainView.addView(textView2);
        mainView.addView(imageView);
        mainView.addView(editText);

        mainView.addView(textViewC);
        mainView.addView(textViewD);

        mainView.addView(btn);

        textView.setText("Testing text");
        mainView.addView(benchmark);
        benchmark.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long benchmarkStart = System.currentTimeMillis();
                for(int i = 0 ; i < 1000; i++){
                    View benchmark = LayoutInflater.from(MainActivity.this).inflate(R.layout.benchmark, null, false);
                }
                Log.d("KittenBinder", "" + (System.currentTimeMillis() - benchmarkStart));

                long start = System.currentTimeMillis();
                for(int i = 0 ; i < 1000; i++){
                    KittenBind.bind(MainActivity.this);
                }
                Log.d("KittenBinder", "" + (System.currentTimeMillis() - start));
            }
        });
    }
}
