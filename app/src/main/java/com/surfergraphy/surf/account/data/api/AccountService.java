package com.surfergraphy.surf.account.data.api;

import com.surfergraphy.surf.login.data.AuthorizationAccountUser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AccountService {

    @FormUrlEncoded
    @POST("api/Account/Register")
    Call<ResponseBody> accountRegister(@Field("Email") String email, @Field("Password") String password, @Field("ConfirmPassword") String confirmPassword, @Field("NickName") String nickName, @Field("PhoneNumber") String phoneNumber);

    @GET("api/Account/UserInfo")
    Call<AuthorizationAccountUser> getAuthorizationAccountUser();
}
