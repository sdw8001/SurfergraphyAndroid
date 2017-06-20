package com.surfergraphy.surfergraphy.photos.data.api;

import com.surfergraphy.surfergraphy.photos.data.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PhotoService {

    @GET("api/Photos")
    Call<List<Photo>> getPhotos();
}
