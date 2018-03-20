package com.surfergraphy.surf.base.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.surfergraphy.surf.R;
import com.surfergraphy.surf.base.AppApplication;
import com.surfergraphy.surf.base.interfaces.ICheckAccessToken;
import com.surfergraphy.surf.base.typeface.TypekitContextWrapper;
import com.surfergraphy.surf.login.Activity_Login;
import com.surfergraphy.surf.login.ViewModel_Login;

public class BaseActivity extends AppCompatActivity implements ICheckAccessToken {

    protected ViewModel_Login viewModelLogin;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.viewModelLogin = ViewModelProviders.of(this).get(ViewModel_Login.class);
        this.checkAccessToken();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void initFirebaseAuth() {
        AppApplication appApplication = (AppApplication) getApplication();
        appApplication.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public FirebaseAuth getFirebaseAuth() {
        return ((AppApplication) getApplication()).firebaseAuth;
    }

    @Override
    public void checkAccessToken() {
        viewModelLogin.getLoginMemberLiveData().observe(this, accessToken -> {
            if (this.getClass() != Activity_Login.class) {
                if (accessToken != null) {
                    if (accessToken.Expired) {
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
