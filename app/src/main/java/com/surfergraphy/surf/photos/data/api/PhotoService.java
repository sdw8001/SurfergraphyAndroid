package com.surfergraphy.surf.photos.data.api;

import com.surfergraphy.surf.photos.data.Photo;
import com.surfergraphy.surf.photos.data.PhotoDate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PhotoService {

    @GET("api/Photo/UserPhotos/{userId}")
    Call<List<Photo>> getPhotoFromUserPhoto(@Path("userId") String userId);

    @GET("api/Photo/LikePhotos/{userId}")
    Call<List<Photo>> getPhotoFromLikePhoto(@Path("userId") String userId);

    @GET("api/Dates")
    Call<List<PhotoDate>> getDates();

    @GET("api/Dates/Place/{place}")
    Call<List<PhotoDate>> getDatesFromPlace(@Path("place") String place);

    @GET("api/Photos/Date/{date}")
    Call<List<Photo>> getPhotos(@Path("date") String date);

    @GET("api/Photos/Place/Date/{place}/{date}")
    Call<List<Photo>> getPlacePhotos(@Path("place") String place, @Path("date") String date);

    @POST("api/Photos/DeleteExpired")
    Call<Void> deleteExpiredPhotos();
}
