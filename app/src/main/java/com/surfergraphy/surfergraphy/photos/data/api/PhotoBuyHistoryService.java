package com.surfergraphy.surfergraphy.photos.data.api;

import com.surfergraphy.surfergraphy.photos.data.PhotoBuyHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PhotoBuyHistoryService {

    @GET("api/PhotoBuyHistories")
    Call<List<PhotoBuyHistory>> getPhotoBuyHistories();

    @GET("api/PhotoBuyHistories/UserPhotos")
    Call<List<PhotoBuyHistory>> getUserPhotoBuyHistories();

    @GET("api/PhotoBuyHistories/UserPhotos/{wavePrice}")
    Call<List<PhotoBuyHistory>> getPhotoBuyHistoryByPhoto(@Path("wavePrice") int photoId);

    @FormUrlEncoded
    @POST("api/PhotoBuyHistories/")
    Call<PhotoBuyHistory> addPhotoBuyHistory(@Field("UserId") String userId, @Field("PhotoId") int photoId);

}
