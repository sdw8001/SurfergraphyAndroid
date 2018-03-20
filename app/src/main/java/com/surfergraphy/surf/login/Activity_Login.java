package com.surfergraphy.surf.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.surfergraphy.surf.R;
import com.surfergraphy.surf.base.ActionCode;
import com.surfergraphy.surf.base.activities.BaseActivity;
import com.surfergraphy.surf.login.data.RequestModel_MemberInfo;
import com.surfergraphy.surf.photos.Activity_Photos;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_Login extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private final static String TAG = "Activity_Login";
    private final static int RC_SIGN_IN_GOOGLE = 1000;

    private CallbackManager facebookCallbackManager;
    private GoogleApiClient mGoogleApiClient;

    @BindView(R.id.edit_text_email)
    EditText editTextAccount;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @BindView(R.id.login_google)
    SignInButton loginGoogleButton;

    @BindView(R.id.login_facebook)
    LoginButton loginFacebookButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setGoogleLogin();
        facebookCallbackManager = CallbackManager.Factory.create();

        viewModelLogin.getLoginMemberLiveData().observe(this, accessToken -> {
            if (accessToken != null) {
                if (!accessToken.Expired) {
                    Intent intent = new Intent(this, Activity_Photos.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        loginGoogleButton.setOnClickListener(view -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                viewModelLogin.loginMember(ActionCode.ACTION_LOGIN_MEMBER, new RequestModel_MemberInfo(acct.getId(), acct.getIdToken(), acct.getEmail(), "G", acct.getDisplayName(), acct.getPhotoUrl().toString()));
            } else {
                // Signed out, show unauthenticated UI.
                Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show();
            }
        } else {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setGoogleButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    private void setGoogleLogin() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setGoogleButtonText(loginGoogleButton, "Google Login");
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "" + connectionResult, Toast.LENGTH_SHORT).show();
    }
}
