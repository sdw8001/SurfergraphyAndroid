package com.surfergraphy.surfergraphy.activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.surfergraphy.surfergraphy.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.animation;
import static android.R.attr.fastScrollPreviewBackgroundLeft;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.notice_top)
    TextView noticeTop;

    @BindView(R.id.button_up)
    Button button_up;

    @BindView(R.id.button_down)
    Button button_down;

    @BindView(R.id.button_reset)
    Button button_reset;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ButterKnife.bind(this);


//        scrollView.setOnTouchListener((v, event) -> true);
        scrollView.post(() -> ObjectAnimator.ofInt(scrollView, "scrollY", noticeTop.getHeight()).setDuration(2000).start());

        button_up.setOnClickListener(v -> ObjectAnimator.ofInt(scrollView, "scrollY", 0).setDuration(1000).start());
        button_down.setOnClickListener(v -> ObjectAnimator.ofInt(scrollView, "scrollY", noticeTop.getHeight()).setDuration(1000).start());
        button_reset.setOnClickListener(v -> {
            scrollView.scrollTo(0, 0);
            Log.e("NOTICE HEIGHT", "" + noticeTop.getHeight());
        });
    }
}
