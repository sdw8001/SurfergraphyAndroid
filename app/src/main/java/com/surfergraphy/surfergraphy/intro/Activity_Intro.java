package com.surfergraphy.surfergraphy.intro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.base.activities.BaseLifecycleActivity;
import com.surfergraphy.surfergraphy.login.Activity_Login;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_Intro extends BaseLifecycleActivity {

    private ViewModel_Intro viewModel_intro;

    @BindView(R.id.image_view_logo)
    ImageView imageView_Logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        viewModel_intro = ViewModelProviders.of(this).get(ViewModel_Intro.class);
        viewModel_intro.getAuthorizationAccountUser();
        imageView_Logo.animate()
                .setStartDelay(500)
                .alpha(1.0f)
                .setDuration(2000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        imageView_Logo.animate()
                                .setStartDelay(500)
                                .alpha(0.0f)
                                .setDuration(2000).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                //TODO: 테스트 시에 사용
//                        Intent intent = new Intent(Activity_Intro.this, TestActivity.class);
                                Intent intent = new Intent(Activity_Intro.this, Activity_Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
    }

}
