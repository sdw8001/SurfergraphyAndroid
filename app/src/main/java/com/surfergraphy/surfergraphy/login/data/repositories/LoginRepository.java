package com.surfergraphy.surfergraphy.login.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surfergraphy.account.data.api.AccountService;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.data.repositories.BaseRepository;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.login.data.AuthorizationAccountUser;
import com.surfergraphy.surfergraphy.login.data.api.LoginService;
import com.surfergraphy.surfergraphy.utils.DateDeserializer;
import com.surfergraphy.surfergraphy.utils.ResponseAction;
import com.surfergraphy.surfergraphy.utils.RetrofitAdapter;

import org.threeten.bp.LocalDateTime;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.authorizationAccountUserDao;

public class LoginRepository extends BaseRepository {

    public LoginRepository(Realm realm) {
        super(realm);
    }

    public void loginAccount(final String identity, final String password) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        LoginService loginService = retrofit.create(LoginService.class);
        final Call<AccessToken> call = loginService.login("password", identity, password);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                new ResponseAction<>(response, new ResponseAction_Default<AccessToken>() {
                    @Override
                    public void error(Response response) {

                    }

                    @Override
                    public void badRequest(Response response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response response) {

                    }

                    @Override
                    public void ok(Response<AccessToken> response) {
                        createOrUpdateAccessToken(response.body());
                        getAuthorizationAccountUser();
                    }

                    @Override
                    public void okCreated(Response response) {

                    }

                    @Override
                    public void unAuthorized(Response response) {

                    }
                });
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {

            }
        });
    }

    public void getAuthorizationAccountUser() {
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
                    public void okCreated(Response<AuthorizationAccountUser> response) {

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
