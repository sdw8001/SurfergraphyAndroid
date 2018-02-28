package com.surfergraphy.surf.photos.data.api;

import com.surfergraphy.surf.photos.data.Photographer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PhotographerService {

    @GET("api/Photographer")
    Call<List<Photographer>> getPhotographers();

    @GET("api/Photographer/{id}")
    Call<Photographer> getPhotographer(@Path("id") String id);
}
