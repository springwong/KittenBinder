package com.spring.example;

import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kittenbinder.DecoBackground;
import kittenbinder.BindContext;
import kittenbinder.DecoEditText;
import kittenbinder.DecoImageView;
import kittenbinder.DecoLinearLayout;
import kittenbinder.DecoPadding;
import kittenbinder.DecoStyle;
import kittenbinder.DecoTextAppearance;
import kittenbinder.DecoTextView;

import com.spring.kittenbinder.binding.KittenBind;

import butterknife.BindView;
import butterknife.ButterKnife;
import kittenbinder.DecoVisibility;

public class MainActivity extends AppCompatActivity {

    @BindContext @DecoLinearLayout
    LinearLayout mainView;

    @BindContext
    @DecoPadding(10)
    @DecoVisibility(View.VISIBLE)
    @DecoBackground(android.R.color.holo_red_dark)
    TextView textView;

    @BindContext
//    @DecoStyle(R.style.sample_text_style)
    @DecoBackground(android.R.color.holo_green_light)
    @DecoTextView(value = R.string.sample1, textSize = R.dimen.text_sample_size, gravity = Gravity.CENTER_HORIZONTAL)
    TextView textView2;

    @BindContext
    @DecoImageView(android.R.drawable.btn_default)
    ImageView imageView;

    @BindContext
    @DecoEditText(hint = R.string.sample1)
    EditText editText;

    @BindContext
    @DecoTextView(R.string.sample1)
//    @DecoTextAppearance(R.style.sample_text_style)
    TextView textViewC;

    //DecoTextView override some seting of textAppearance
    @BindContext
    @DecoTextView(value = R.string.sample1, textSize = R.dimen.text_sample_size, textColor = R.color.colorPrimaryDark)
//    @DecoTextAppearance(R.style.sample_text_style)
    TextView textViewD;

    @BindContext
    @DecoBackground(android.R.drawable.btn_default)
    @DecoTextView(value = R.string.sample1, gravity = Gravity.CENTER, textColor = R.color.colorPrimaryDark)
    @DecoPadding(10)
    TextView btn;

    @BindView(R.id.benchA)
    TextView benchA;
    @BindView(R.id.benchB)
    TextView benchB;
    @BindView(R.id.benchC)
    ImageView benchC;
    @BindView(R.id.benchD)
    EditText benchD;
    @BindView(R.id.benchE)
    TextView benchE;
    @BindView(R.id.benchF)
    TextView benchF;
    @BindView(R.id.btn)
    TextView benchMarkButton;

    @BindContext
    CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View benchmark = LayoutInflater.from(this).inflate(R.layout.benchmark, null, false);
        KittenBind.bind(this);
        ButterKnife.bind(this, benchmark);
//        new MainActivity_ViewDecorator().bind(this, this);
        setContentView(mainView);

        mainView.setOrientation(LinearLayout.VERTICAL);

        mainView.addView(customView);
        mainView.addView(getConstraintLayout());
        mainView.addView(textView);
        mainView.addView(textView2);
        mainView.addView(imageView);
        mainView.addView(editText);

        mainView.addView(textViewC);
        mainView.addView(textViewD);

        mainView.addView(btn);

        customView.textView.setText("custom view");

        textView.setText("Testing text");
        mainView.addView(benchmark);
        benchMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long benchmarkStart = System.currentTimeMillis();
                for(int i = 0 ; i < 1000; i++){
                    View benchmark = LayoutInflater.from(MainActivity.this).inflate(R.layout.benchmark, null, false);
                    ButterKnife.bind(this, benchmark);
                }
                Log.d("KittenBinder", "Layout Inflate:" + (System.currentTimeMillis() - benchmarkStart));

                long decoratorTime = System.currentTimeMillis();
                for(int i = 0 ; i < 1000; i++){
                    new MainActivity_ViewDecorator().bind(MainActivity.this, MainActivity.this);
                }
                Log.d("KittenBinder", "New Decorator:" + (System.currentTimeMillis() - decoratorTime));

                long start = System.currentTimeMillis();
                for(int i = 0 ; i < 1000; i++){
                    KittenBind.bind(MainActivity.this);
                }
                Log.d("KittenBinder", "Kitten Builder:" + (System.currentTimeMillis() - start));

                long test = System.currentTimeMillis();
                for(int i = 0 ; i < 1000; i++){
                    createByCode();
                }
                Log.d("KittenBinder", "Pure Coding:" + (System.currentTimeMillis() - test));

            }
        });
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
    ViewGroup getConstraintLayout(){
//        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        ConstraintSet set = new ConstraintSet();
//        set.clone(constraintLayout);
        RelativeLayout relativeLayout = new RelativeLayout(this);

        TextView textView = new TextView(this);
        textView.setId(textView.generateViewId());
        textView.setText("123");
        textView.setMinLines(7);
        textView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        TextView textView2 = new TextView(this);
        textView2.setId(textView.generateViewId());
        textView2.setText("123");
        textView2.setMinLines(2);
        textView2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        TextView textView3 = new TextView(this);
        textView3.setId(textView.generateViewId());
        textView3.setText("ew ijoewjovwj iowe");
        textView3.setMinLines(4);

        relativeLayout.addView(textView);
        relativeLayout.addView(textView2);
        relativeLayout.addView(textView3);

//        constraintLayout.addView(textView);
//        constraintLayout.addView(textView2);
        Log.d("TEst", "Id:"+textView.getId());
        Log.d("TEst", "Id:"+textView2.getId());

//        set.centerHorizontally(textView.getId(), textView2.getId());
        set.connect(textView.getId(), ConstraintSet.RIGHT, textView2.getId(), ConstraintSet.LEFT);
//        set.applyTo(constraintLayout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, textView.getId());
        layoutParams.leftMargin = 10;
        textView2.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.addRule(RelativeLayout.BELOW, textView.getId());
        layoutParams1.addRule(RelativeLayout.BELOW, textView2.getId());
        textView3.setLayoutParams(layoutParams1);

        return relativeLayout;
    }
}
