package com.surfergraphy.surfergraphy.photos.data.api;

import com.surfergraphy.surfergraphy.photos.data.PhotoSaveHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PhotoSaveHistoryService {

    @GET("api/PhotoSaveHistories")
    Call<List<PhotoSaveHistory>> getPhotoSaveHistories();

    @GET("api/PhotoSaveHistories/UserPhotos")
    Call<List<PhotoSaveHistory>> getUserPhotoSaveHistories();

    @GET("api/PhotoSaveHistories/UserPhotos/{wavePrice}")
    Call<List<PhotoSaveHistory>> getPhotoSaveHistoryByPhoto(@Path("wavePrice") int photoId);

    @FormUrlEncoded
    @POST("api/PhotoSaveHistories/")
    Call<PhotoSaveHistory> addPhotoSaveHistory(@Field("UserId") String userId, @Field("PhotoId") int photoId);

}
