package com.surfergraphy.surfergraphy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.surfergraphy.surfergraphy.BaseLifecycleActivity;
import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.login.Activity_Login;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_Intro extends BaseLifecycleActivity {

    @BindView(R.id.textView1)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        //TODO: 테스트 시에 사용
//        Intent intent = new Intent(Activity_Intro.this, TestActivity.class);
        Intent intent = new Intent(Activity_Intro.this, Activity_Login.class);
        startActivity(intent);
        finish();
    }

}
