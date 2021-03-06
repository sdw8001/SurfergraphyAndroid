package com.surfergraphy.surf.intro;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.surfergraphy.surf.R;
import com.surfergraphy.surf.login.Activity_Login;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_Intro extends AppCompatActivity {

    private ViewModel_Intro viewModel_intro;

    @BindView(R.id.image_view_logo)
    ImageView imageView_Logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        viewModel_intro = ViewModelProviders.of(this).get(ViewModel_Intro.class);
        viewModel_intro.syncLoginMember();
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
                                .setDuration(2000)
                                .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                Random random = new Random();
                                int[] introImg = {R.drawable.loading_images_01, R.drawable.loading_images_02};
                                Glide.with(Activity_Intro.this).load(introImg[random.nextInt(2)]).into(imageView_Logo);
                                imageView_Logo.animate()
                                        .alpha(1.0f)
                                        .setDuration(300)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                imageView_Logo.animate()
                                                        .setStartDelay(3000)
                                                        .alpha(0.0f)
                                                        .setDuration(300)
                                                        .setListener(new AnimatorListenerAdapter() {
                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                super.onAnimationEnd(animation);
                                                                TedPermission.with(Activity_Intro.this)
                                                                        .setPermissionListener(permissionlistener)
                                                                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                                                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                                        .check();
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
                    }
                });
    }

    private void startLoginActivity() {
        //TODO: 테스트 시에 사용
//        Intent intent = new Intent(Activity_Intro.this, TestActivity.class);

        Intent intent = new Intent(Activity_Intro.this, Activity_Login.class);
        startActivity(intent);
        finish();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            startLoginActivity();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            startLoginActivity();
        }
    };
}
