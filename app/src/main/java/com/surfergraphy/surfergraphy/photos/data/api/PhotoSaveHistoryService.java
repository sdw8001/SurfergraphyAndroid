package com.surfergraphy.surfergraphy.photos.data.api;

import com.surfergraphy.surfergraphy.photos.data.PhotoSaveHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PhotoSaveHistoryService {

    @GET("api/PhotoSaveHistories")
    Call<List<PhotoSaveHistory>> getPhotoSaveHistories();

    @FormUrlEncoded
    @POST("api/PhotoSaveHistories/")
    Call<PhotoSaveHistory> addPhotoSaveHistory(@Field("UserId") String userId, @Field("PhotoId") int photoId);

}
