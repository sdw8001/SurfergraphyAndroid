package com.surfergraphy.surfergraphy.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.account.Activity_AccountRegister;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.photos.Activity_Photos;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_Login extends BaseActivity {

    @BindView(R.id.edit_text_email)
    EditText editTextAccount;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.buttonLogin)
    View buttonLogin;
    @BindView(R.id.buttonAccountRegister)
    View buttonAccountRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        viewModelLogin.getAccessToken().observe(this, accessToken -> {
            if (accessToken != null) {
                if (!accessToken.expired) {
                    Intent intent = new Intent(this, Activity_Photos.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        buttonLogin.setOnClickListener(v -> viewModelLogin.loginAccount(editTextAccount.getText().toString(), editTextPassword.getText().toString()));
        buttonAccountRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_AccountRegister.class);
            startActivity(intent);
        });
    }
}
