package com.surfergraphy.surf.photos.data.api;

import com.surfergraphy.surf.photos.data.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PhotoService {

    @GET("api/Photos")
    Call<List<Photo>> getPhotos();

    @GET("api/Photos/Place/{place}")
    Call<List<Photo>> getPlacePhotos(@Path("place") String place);

    @POST("api/Photos/DeleteExpired")
    Call<Void> deleteExpiredPhotos();
}
