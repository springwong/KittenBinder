package com.spring.example;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kittenbinder.BindBackground;
import kittenbinder.BindContext;
import kittenbinder.BindEditText;
import kittenbinder.BindImageView;
import kittenbinder.BindLinearLayout;
import kittenbinder.BindPadding;
import kittenbinder.BindTextAppearance;
import kittenbinder.BindTextView;

import com.spring.kittenbinder.binding.KittenBind;

import butterknife.BindView;
import butterknife.ButterKnife;
import kittenbinder.BindTest;
import kittenbinder.BindVisibility;

public class MainActivity extends AppCompatActivity {

    @BindContext @BindLinearLayout
    LinearLayout mainView;

    @BindContext
    @BindPadding(10)
    @BindVisibility(View.VISIBLE)
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

    @BindView(R.id.benchA)
    TextView benchA;
//    @BindView(R.id.benchB)
//    TextView benchB;
//    @BindView(R.id.benchC)
//    ImageView benchC;
//    @BindView(R.id.benchD)
//    EditText benchD;
//    @BindView(R.id.benchE)
//    TextView benchE;
//    @BindView(R.id.benchF)
//    TextView benchF;
//    @BindView(R.id.btn)
//    TextView benchMarkButton;

    @BindTest
    TextView testView;
    @BindTest
    ImageView string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View benchmark = LayoutInflater.from(this).inflate(R.layout.benchmark, null, false);
        KittenBind.bind(this);
        ButterKnife.bind(this, benchmark);
        setContentView(mainView);

        mainView.setOrientation(LinearLayout.VERTICAL);
        new MainActivity_ViewDecorator().bind(this, this);
        testView.setText("I am alive from binding!");

        mainView.addView(testView);
        mainView.addView(textView);
        mainView.addView(textView2);
        mainView.addView(imageView);
        mainView.addView(editText);

        mainView.addView(textViewC);
        mainView.addView(textViewD);

        mainView.addView(btn);

        textView.setText("Testing text");
        mainView.addView(benchmark);
//        benchMarkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                long benchmarkStart = System.currentTimeMillis();
//                for(int i = 0 ; i < 1000; i++){
//                    View benchmark = LayoutInflater.from(MainActivity.this).inflate(R.layout.benchmark, null, false);
//                    ButterKnife.bind(this, benchmark);
//                }
//                Log.d("KittenBinder", "Layout Inflate:" + (System.currentTimeMillis() - benchmarkStart));
//
//                long start = System.currentTimeMillis();
//                for(int i = 0 ; i < 1000; i++){
//                    KittenBind.bind(MainActivity.this);
//                }
//                Log.d("KittenBinder", "Kitten Builder:" + (System.currentTimeMillis() - start));
//
//                long test = System.currentTimeMillis();
//                for(int i = 0 ; i < 1000; i++){
//                    createByCode();
//                }
//                Log.d("KittenBinder", "Pure Coding:" + (System.currentTimeMillis() - test));
//
//            }
//        });
    }

    private void createByCode(){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView a= new TextView(this);
        a.setPadding(10,10,10,10);
        a.setText("testing text");
        a.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));

        TextView b= new TextView(this);
        b.setText(R.string.sample1);
        b.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
        b.setGravity(Gravity.CENTER_HORIZONTAL);
        b.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_sample_size));
        ImageView c= new ImageView(this);
        c.setImageResource(android.R.drawable.btn_default);
        EditText d= new EditText(this);
        d.setHint(R.string.sample1);
        EditText e= new EditText(this);
        e.setHint(R.string.sample1);
        e.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
        EditText f= new EditText(this);
        f.setHint(R.string.sample1);
        f.setTextAppearance(android.R.style.TextAppearance_Material_Headline);
        f.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_sample_size));
        f.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        TextView btn= new TextView(this);
        f.setHint(R.string.sample1);
        f.setPadding(10,10,10,10);
        f.setBackgroundResource(android.R.drawable.btn_default);
        f.setGravity(Gravity.CENTER);
        f.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }
}
