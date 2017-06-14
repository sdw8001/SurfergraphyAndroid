package com.surfergraphy.surfergraphy.login;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.surfergraphy.surfergraphy.BaseLifecycleActivity;
import com.surfergraphy.surfergraphy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_Login extends BaseLifecycleActivity {

    private LoginViewModel loginViewModel;

    @BindView(R.id.editTextAccount)
    EditText editTextAccount;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    @BindView(R.id.cardViewAccessInfo)
    CardView cardViewAccessInfo;
    @BindView(R.id.editTextUserName)
    TextView textViewUserName;
    @BindView(R.id.editTextAccessToken)
    TextView textViewAccessToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginViewModel.getAccessToken().observe(this, accessToken -> {
            if (accessToken != null) {
                textViewAccessToken.setText(accessToken.accessToken);
                textViewUserName.setText(accessToken.userName);
            }
        });

        buttonLogin.setOnClickListener(v -> loginViewModel.loginAccount(editTextAccount.getText().toString(), editTextPassword.getText().toString()));
    }
}
