package com.surfergraphy.surf.base.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surf.account.data.api.AccountService;
import com.surfergraphy.surf.base.data.ActionResponse;
import com.surfergraphy.surf.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surf.login.data.AuthorizationAccountUser;
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
import static com.surfergraphy.surf.utils.RealmUtils.actionResponseModel;
import static com.surfergraphy.surf.utils.RealmUtils.authorizationAccountUserDao;

public class BaseRepository {
    protected Realm realm;

    public BaseRepository(final Realm realm) {
        if (realm == null) {
            this.realm = Realm.getDefaultInstance();
        } else {
            this.realm = realm;
        }
    }

    protected ActionResponse createOrUpdateActionResponse(final ActionResponse actionResponse) {
        return actionResponseModel(realm).createOrUpdate(actionResponse);
    }

    protected void updateExpiredAccessToken(boolean expired) {
        accessTokenModel(realm).updateExpiredAccessToken(expired);
    }

    protected AuthorizationAccountUser createOrUpdateAuthorizationAccountUser(final AuthorizationAccountUser authorizationAccountUser) {
        return authorizationAccountUserDao(realm).createOrUpdate(authorizationAccountUser);
    }

    public void syncAuthorizationAccountUser() {
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
}
