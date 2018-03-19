package com.surfergraphy.surf.login.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surf.base.ActionCode;
import com.surfergraphy.surf.base.data.ActionResponse;
import com.surfergraphy.surf.base.data.repositories.BaseRepository;
import com.surfergraphy.surf.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surf.login.data.LoginMember;
import com.surfergraphy.surf.login.data.Member;
import com.surfergraphy.surf.login.data.RequestModel_MemberInfo;
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

import static com.surfergraphy.surf.utils.RealmUtils.loginMemberModel;

public class LoginRepository extends BaseRepository {

    public LoginRepository(Realm realm) {
        super(realm);
    }

    public void joinMember(final int actionCode, final RequestModel_MemberInfo requestModel) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        LoginService service = retrofit.create(LoginService.class);

        final Call<Member> call = service.joinMember(requestModel.Id, requestModel.Email, requestModel.JoinType, requestModel.Name, requestModel.ImageUrl);
        call.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                new ResponseAction<>(response, new ResponseAction_Default<Member>() {
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
                    public void ok(Response<Member> response) {
                        // 가입성공. 로그인 다시 호출
                        loginMember(ActionCode.ACTION_LOGIN_MEMBER, requestModel);
                    }

                    @Override
                    public void okCreated(Response response) {
                        // 가입성공. 로그인 다시 호출
                        loginMember(ActionCode.ACTION_LOGIN_MEMBER, requestModel);
                    }

                    @Override
                    public void unAuthorized(Response response) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {

            }
        });
    }

    public void loginMember(final int actionCode, final RequestModel_MemberInfo requestModel) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        LoginService service = retrofit.create(LoginService.class);
        final Call<LoginMember> call = service.loginMember(requestModel.Id, requestModel.LoginToken);
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
                        // 로그인정보 회원이 가입되있지 않은경우 가입요청.
                        joinMember(ActionCode.ACTION_JOIN_MEMBER, requestModel);
                    }

                    @Override
                    public void ok(Response<LoginMember> response) {
                        // 로그인 성공 (로그인 토큰 저장)
                        createOrUpdateLoginMember(response.body());
                        ActionResponse actionResponse = new ActionResponse();
                        actionResponse.setActionCode(actionCode);
                        actionResponse.setResultCode(response.code());
                        actionResponse.setMessage(response.message());
                        createOrUpdateActionResponse(actionResponse);
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
            public void onFailure(Call<LoginMember> call, Throwable t) {

            }
        });
    }
}
