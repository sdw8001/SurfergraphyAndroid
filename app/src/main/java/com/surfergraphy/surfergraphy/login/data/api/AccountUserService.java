package com.surfergraphy.surfergraphy.login.data.api;

import com.surfergraphy.surfergraphy.login.data.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by ddfactory on 2017-06-13.
 */

public interface AccountUserService {

    @FormUrlEncoded
    @POST("Token")
    Call<AccessToken> login(@Field("grant_type") String grantType, @Field("username") String userName, @Field("password") String password);
}
