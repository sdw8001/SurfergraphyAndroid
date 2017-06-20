package com.surfergraphy.surfergraphy.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.surfergraphy.surfergraphy.BaseLifecycleActivity;
import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.photos.Activity_Photos;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_Login extends BaseLifecycleActivity {

    private ViewModel_Login viewModelLogin;

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

        viewModelLogin = ViewModelProviders.of(this).get(ViewModel_Login.class);

        viewModelLogin.getAccessToken().observe(this, accessToken -> {
            if (accessToken != null) {
                textViewAccessToken.setText(accessToken.accessToken);
                textViewUserName.setText(accessToken.userName);

                Intent intent = new Intent(Activity_Login.this, Activity_Photos.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(v -> viewModelLogin.loginAccount(editTextAccount.getText().toString(), editTextPassword.getText().toString()));
    }
}
