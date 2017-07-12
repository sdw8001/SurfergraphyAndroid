package com.surfergraphy.surfergraphy.login.data.api;

import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.login.data.AuthorizationAccountUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AccountUserService {

    @FormUrlEncoded
    @POST("Token")
    Call<AccessToken> login(@Field("grant_type") String grantType, @Field("username") String userName, @Field("password") String password);

    @GET("api/Account/UserInfo")
    Call<AuthorizationAccountUser> getAuthorizationAccountUser();
}
