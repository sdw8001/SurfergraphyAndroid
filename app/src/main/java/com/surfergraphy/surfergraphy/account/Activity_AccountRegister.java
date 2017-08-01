package com.surfergraphy.surfergraphy.account;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.base.ActionCode;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.utils.ResponseAction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_AccountRegister extends BaseActivity {

    private ViewModel_AccountRegister viewModel_AccountRegister;

    @BindView(R.id.edit_text_email)
    EditText editText_Email;
    @BindView(R.id.edit_text_password)
    EditText editText_Password;
    @BindView(R.id.edit_text_password_confirm)
    EditText editText_PasswordConfirm;
    @BindView(R.id.edit_text_nick_name)
    EditText editText_NickName;
    @BindView(R.id.edit_text_phone_number)
    EditText editText_PhoneNumber;
    @BindView(R.id.button_save)
    Button button_AccountRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel_AccountRegister = ViewModelProviders.of(this).get(ViewModel_AccountRegister.class);

        button_AccountRegister.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editText_Email.getText())) {
                Toast.makeText(this, "아이디(Email)를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(editText_Password.getText())) {
                Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(editText_PasswordConfirm.getText())) {
                Toast.makeText(this, "확인 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(editText_NickName.getText())) {
                Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(editText_PhoneNumber.getText())) {
                Toast.makeText(this, "핸드폰번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            } else {
                viewModel_AccountRegister.requestAccountRegister(ActionCode.ACTION_ACCOUNT_REGISTER, editText_Email.getText().toString(),
                        editText_Password.getText().toString(), editText_PasswordConfirm.getText().toString(), editText_NickName.getText().toString(), editText_PhoneNumber.getText().toString());
            }
        });
        viewModel_AccountRegister.getActionResponse(ActionCode.ACTION_ACCOUNT_REGISTER).observe(this, actionResponse -> {
            if (actionResponse != null) {
                switch (actionResponse.getResultCode()) {
                    case ResponseAction.HTTP_200_OK:
                        Toast.makeText(this, actionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case ResponseAction.HTTP_400_BAD_REQUEST:
                        Toast.makeText(this, actionResponse.getDetailMessages(), Toast.LENGTH_SHORT).show();
                        break;
                }
                viewModel_AccountRegister.expiredActionToken(actionResponse.getActionCode());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void checkAccessToken() {
        // checkAccessToken 오버라이드해서 check 로직 제거
    }
}
