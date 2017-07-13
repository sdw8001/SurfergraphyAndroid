package com.surfergraphy.surfergraphy.base.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.surfergraphy.surfergraphy.login.Activity_Login;
import com.surfergraphy.surfergraphy.login.ViewModel_Login;

public class BaseActivity extends BaseLifecycleActivity {

    protected ViewModel_Login viewModelLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.viewModelLogin = ViewModelProviders.of(this).get(ViewModel_Login.class);

        viewModelLogin.getAccessToken().observe(this, accessToken -> {
            if (this.getClass() != Activity_Login.class) {
                if (accessToken != null) {
                    if (accessToken.expired) {
                        Toast.makeText(getBaseContext(), "토큰 만료. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, Activity_Login.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
