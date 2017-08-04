package com.surfergraphy.surfergraphy.photos.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.data.repositories.BaseRepository;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.photos.data.api.PhotoService;
import com.surfergraphy.surfergraphy.utils.DateDeserializer;
import com.surfergraphy.surfergraphy.utils.ResponseAction;
import com.surfergraphy.surfergraphy.utils.RetrofitAdapter;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoSaveHistory;

public class PhotoRepository extends BaseRepository {

    public PhotoRepository(Realm realm) {
        super(realm);
    }

    public void getPhotos() {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotoService photoService = retrofit.create(PhotoService.class);

        final Call<List<Photo>> call = photoService.getPhotos();
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                new ResponseAction<>(response, new ResponseAction_Default<List<Photo>>() {
                    @Override
                    public void error(Response<List<Photo>> response) {

                    }

                    @Override
                    public void badRequest(Response<List<Photo>> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<List<Photo>> response) {

                    }

                    @Override
                    public void ok(Response<List<Photo>> response) {
                        createOrUpdatePhotos(response.body());
                    }

                    @Override
                    public void okCreated(Response<List<Photo>> response) {

                    }

                    @Override
                    public void unAuthorized(Response<List<Photo>> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    public void getPlacePhotos(final String place) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotoService photoService = retrofit.create(PhotoService.class);

        final Call<List<Photo>> call = photoService.getPlacePhotos(place);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                new ResponseAction<>(response, new ResponseAction_Default<List<Photo>>() {
                    @Override
                    public void error(Response<List<Photo>> response) {

                    }

                    @Override
                    public void badRequest(Response<List<Photo>> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<List<Photo>> response) {

                    }

                    @Override
                    public void ok(Response<List<Photo>> response) {
                        createOrUpdatePhotos(response.body());
                    }

                    @Override
                    public void okCreated(Response<List<Photo>> response) {

                    }

                    @Override
                    public void unAuthorized(Response<List<Photo>> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    public void deletePhotos() {
        realm.beginTransaction();
        photoModel(realm).deletePhotos();
        realm.commitTransaction();
    }

    public void deletePlacePhotos(final String place) {
        realm.beginTransaction();
        photoModel(realm).deletePlacePhotos(place);
        realm.commitTransaction();
    }

    private void createOrUpdatePhotos(final List<Photo> photos) {
        if (photos == null)
            return;

        realm.beginTransaction();
        for (Photo photo : photos) {
            photoModel(realm).createOrUpdate(photo);
        }
        realm.commitTransaction();
    }
}
