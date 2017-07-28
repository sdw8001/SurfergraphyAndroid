package com.surfergraphy.surfergraphy.login.data.repositories;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surfergraphy.account.data.api.AccountService;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.login.data.AuthorizationAccountUser;
import com.surfergraphy.surfergraphy.login.data.api.LoginService;
import com.surfergraphy.surfergraphy.utils.DateDeserializer;
import com.surfergraphy.surfergraphy.utils.ResponseAction;
import com.surfergraphy.surfergraphy.utils.RetrofitAdapter;

import org.threeten.bp.LocalDateTime;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.authorizationAccountUserDao;

public class LoginRepository {
    private Realm realm;

    public void loginAccount(final Realm realm, final String identity, final String password) {
        this.realm = realm;

        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        LoginService loginService = retrofit.create(LoginService.class);
        final Call<AccessToken> call = loginService.login("password", identity, password);
        new NetworkCall().execute(call);
    }

    private class NetworkCall extends AsyncTask<Call, Void, AccessToken> {
        @Override
        protected AccessToken doInBackground(Call[] params) {
            try {
                Call<AccessToken> call = params[0];
                Response<AccessToken> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AccessToken result) {
            createOrUpdateAccessToken(result);
            getAuthorizationAccountUser(realm);
        }
    }

    public void getAuthorizationAccountUser(final Realm realm) {
        this.realm = realm;

        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        AccountService accountService = retrofit.create(AccountService.class);

        final Call<AuthorizationAccountUser> call = accountService.getAuthorizationAccountUser();
        call.enqueue(new Callback<AuthorizationAccountUser>() {
            @Override
            public void onResponse(Call<AuthorizationAccountUser> call, Response<AuthorizationAccountUser> response) {
                new ResponseAction<>(response, new ResponseAction_Default<AuthorizationAccountUser>() {
                    @Override
                    public void error(Response<AuthorizationAccountUser> response) {

                    }

                    @Override
                    public void badRequest(Response<AuthorizationAccountUser> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<AuthorizationAccountUser> response) {

                    }

                    @Override
                    public void ok(Response<AuthorizationAccountUser> response) {
                        createOrUpdateAuthorizationAccountUser(response.body());
                    }

                    @Override
                    public void unAuthorized(Response<AuthorizationAccountUser> response) {
                        updateExpiredAccessToken(true);
                    }
                });
            }

            @Override
            public void onFailure(Call<AuthorizationAccountUser> call, Throwable t) {
                new Throwable("getAuthorizationAccountUser Failed", t);
            }
        });
    }

    private AuthorizationAccountUser createOrUpdateAuthorizationAccountUser(final AuthorizationAccountUser authorizationAccountUser) {
        realm.beginTransaction();
        AuthorizationAccountUser accountUser = authorizationAccountUserDao(realm).createOrUpdate(authorizationAccountUser);
        realm.commitTransaction();
        return accountUser;
    }

    private AccessToken createOrUpdateAccessToken(final AccessToken accessToken) {
        realm.beginTransaction();
        AccessToken token = accessTokenModel(realm).createOrUpdate(accessToken);
        realm.commitTransaction();
        return token;
    }

    private void updateExpiredAccessToken(boolean expired) {
        accessTokenModel(realm).updateExpiredAccessToken(expired);
    }
}
