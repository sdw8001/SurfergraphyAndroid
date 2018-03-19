package com.surfergraphy.surf.base.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surf.base.data.ActionResponse;
import com.surfergraphy.surf.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surf.login.data.LoginMember;
import com.surfergraphy.surf.login.data.Member;
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

import static com.surfergraphy.surf.utils.RealmUtils.actionResponseModel;
import static com.surfergraphy.surf.utils.RealmUtils.loginMemberModel;

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

    protected void updateExpiredLoginMember(boolean expired) {
        loginMemberModel(realm).updateExpiredLoginMember(expired);
    }

    protected LoginMember createOrUpdateLoginMember(final LoginMember loginMember) {
        realm.beginTransaction();
        LoginMember member = loginMemberModel(realm).createOrUpdate(loginMember);
        realm.commitTransaction();
        return member;
    }

    public void syncLoginMember() {
        LoginMember currentLoginMember = realm.where(LoginMember.class).findFirst();
        if (currentLoginMember != null) {
            String id = currentLoginMember.Id;

            Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
            Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
            LoginService service = retrofit.create(LoginService.class);

            final Call<LoginMember> call = service.getMember(id);
            call.enqueue(new Callback<LoginMember>() {
                @Override
                public void onResponse(Call<LoginMember> call, Response<LoginMember> response) {
                    new ResponseAction<>(response, new ResponseAction_Default<LoginMember>() {
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
                        public void ok(Response<LoginMember> response) {
                            createOrUpdateLoginMember(response.body());
                        }

                        @Override
                        public void okCreated(Response response) {

                        }

                        @Override
                        public void unAuthorized(Response response) {
                            updateExpiredLoginMember(true);
                        }
                    });
                }

                @Override
                public void onFailure(Call<LoginMember> call, Throwable t) {

                }
            });
        }
    }
}
