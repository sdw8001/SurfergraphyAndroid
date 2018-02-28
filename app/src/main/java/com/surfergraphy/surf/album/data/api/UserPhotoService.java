package com.surfergraphy.surf.album.data.api;

import com.surfergraphy.surf.album.data.UserPhoto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserPhotoService {

    @GET("api/UserPhotos")
    Call<List<UserPhoto>> getUserPhotos();

    @FormUrlEncoded
    @POST("api/UserPhotos/")
    Call<UserPhoto> addUserPhoto(@Field("UserId") String userId, @Field("PhotoId") int photoId, @Field("PhotoSaveHistoryId") int photoSaveHistoryId, @Field("PhotoBuyHistoryId") int photoBuyHistoryId);
}
