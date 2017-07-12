package com.surfergraphy.surfergraphy.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.surfergraphy.surfergraphy.base.activities.BaseLifecycleActivity;
import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.photos.Activity_Photos;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class Activity_Login extends BaseLifecycleActivity {

    private ViewModel_Login viewModel_login;

    @BindView(R.id.editTextAccount)
    EditText editTextAccount;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        viewModel_login = ViewModelProviders.of(this).get(ViewModel_Login.class);

        viewModel_login.getAccessToken().observe(this, accessToken -> {
            if (accessToken != null) {
                if (accessToken.expired) {
                    Toast.makeText(getBaseContext(), "토큰 만료. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Activity_Login.this, Activity_Photos.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        buttonLogin.setOnClickListener(v -> viewModel_login.loginAccount(editTextAccount.getText().toString(), editTextPassword.getText().toString()));
    }

}
