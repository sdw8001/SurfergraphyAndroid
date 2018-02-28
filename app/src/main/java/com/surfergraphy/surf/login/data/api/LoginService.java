package com.surfergraphy.surf.login.data.api;

import com.surfergraphy.surf.login.data.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {

    @FormUrlEncoded
    @POST("Token")
    Call<AccessToken> login(@Field("grant_type") String grantType, @Field("username") String userName, @Field("password") String password);
}
