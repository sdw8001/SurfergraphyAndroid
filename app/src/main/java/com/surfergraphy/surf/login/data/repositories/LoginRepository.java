package com.surfergraphy.surf.login.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surf.base.data.ActionResponse;
import com.surfergraphy.surf.base.data.repositories.BaseRepository;
import com.surfergraphy.surf.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surf.login.data.AccessToken;
import com.surfergraphy.surf.login.data.api.LoginService;
import com.surfergraphy.surf.utils.DateDeserializer;
import com.surfergraphy.surf.utils.ResponseAction;
import com.surfergraphy.surf.utils.RetrofitAdapter;

import org.threeten.bp.LocalDateTime;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.surfergraphy.surf.utils.RealmUtils.accessTokenModel;

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
                        syncAuthorizationAccountUser();
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

    private AccessToken createOrUpdateAccessToken(final AccessToken accessToken) {
        realm.beginTransaction();
        AccessToken token = accessTokenModel(realm).createOrUpdate(accessToken);
        realm.commitTransaction();
        return token;
    }
}
