package com.surfergraphy.surf.login.data.api;

import com.surfergraphy.surf.login.data.LoginMember;
import com.surfergraphy.surf.login.data.Member;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginService {

    @FormUrlEncoded
    @POST("api/Members")
    Call<Member> joinMember(@Field("Id") String id, @Field("Email") String email, @Field("JoinType") String joinType, @Field("Name") String name, @Field("ImageUrl") String imageUrl);

    @FormUrlEncoded
    @POST("api/Members/Login")
    Call<LoginMember> loginMember(@Field("Id") String Id, @Field("LoginToken") String loginToken);

    @GET("api/Members/{id}")
    Call<LoginMember> getMember(@Path("id") String id);
}
