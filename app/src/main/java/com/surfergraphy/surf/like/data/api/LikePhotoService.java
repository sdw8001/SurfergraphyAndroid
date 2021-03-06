package com.surfergraphy.surf.like.data.api;

import com.surfergraphy.surf.like.data.LikePhoto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LikePhotoService {

    @GET("api/LikePhotos/UserPhotos/{memberId}")
    Call<List<LikePhoto>> getUserLikePhotos(@Path("memberId") String memberId);

    @FormUrlEncoded
    @POST("api/LikePhotos/")
    Call<LikePhoto> likePhoto(@Field("UserId") String userId, @Field("PhotoId") int photoId);

    @DELETE("api/LikePhotos/{id}")
    Call<LikePhoto> cancelLikePhoto(@Path("id")  int id);
}
