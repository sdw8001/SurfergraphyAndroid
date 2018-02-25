package com.surfergraphy.surfergraphy.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.surfergraphy.surfergraphy.R;
import com.surfergraphy.surfergraphy.account.Activity_AccountRegister;
import com.surfergraphy.surfergraphy.base.BaseIntentKey;
import com.surfergraphy.surfergraphy.base.BaseType;
import com.surfergraphy.surfergraphy.base.activities.BaseActivity;
import com.surfergraphy.surfergraphy.photos.Activity_Photos;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_Login extends BaseActivity {

    private CallbackManager facebookCallbackManager;

    @BindView(R.id.edit_text_email)
    EditText editTextAccount;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.buttonLogin)
    View buttonLogin;
    @BindView(R.id.buttonAccountRegister)
    View buttonAccountRegister;

    @BindView(R.id.login_button)
    LoginButton loginFacebookButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        facebookCallbackManager = CallbackManager.Factory.create();

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

        loginFacebookButton.setReadPermissions("email");


        // Callback registration
        loginFacebookButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                loginFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loginFacebook(AccessToken accessToken) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.v("result", object.toString());
//                try {
//                    Intent intent = new Intent(Activity_Login.this, SignInSnsActivity.class);
//                    intent.putExtra(BaseIntentKey.SignInType, BaseType.SignIn.Facebook);
//                    intent.putExtra(BaseIntentKey.Identity, object.getString("id"));
//                    intent.putExtra(BaseIntentKey.Name, object.getString("name"));
//                    intent.putExtra(BaseIntentKey.Email, object.getString("email"));
//                    intent.putExtra(BaseIntentKey.ImageUrl, Profile.getCurrentProfile().getProfilePictureUri(160, 160).toString());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }
}
