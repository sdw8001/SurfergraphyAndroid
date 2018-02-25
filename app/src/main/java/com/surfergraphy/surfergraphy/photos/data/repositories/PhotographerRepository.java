package com.surfergraphy.surfergraphy.photos.data.repositories;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.data.repositories.BaseRepository;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surfergraphy.photos.data.Photographer;
import com.surfergraphy.surfergraphy.photos.data.api.PhotoService;
import com.surfergraphy.surfergraphy.photos.data.api.PhotographerService;
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

import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.photographerModel;

public class PhotographerRepository extends BaseRepository {

    private static PhotographerRepository instance;
    public static PhotographerRepository getInstance(Realm realm) {
        if (instance == null)
            instance = new PhotographerRepository(realm);
        return instance;
    }

    public PhotographerRepository(Realm realm) {
        super(realm);
    }

    public void getPhotographers() {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotographerService photographerService = retrofit.create(PhotographerService.class);

        final Call<List<Photographer>> call = photographerService.getPhotographers();
        call.enqueue(new Callback<List<Photographer>>() {
            @Override
            public void onResponse(Call<List<Photographer>> call, Response<List<Photographer>> response) {
                new ResponseAction<>(response, new ResponseAction_Default<List<Photographer>>() {
                    @Override
                    public void error(Response<List<Photographer>> response) {

                    }

                    @Override
                    public void badRequest(Response<List<Photographer>> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<List<Photographer>> response) {

                    }

                    @Override
                    public void ok(Response<List<Photographer>> response) {
                        createOrUpdatePhotographers(response.body());
                    }

                    @Override
                    public void okCreated(Response<List<Photographer>> response) {

                    }

                    @Override
                    public void unAuthorized(Response<List<Photographer>> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Photographer>> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    public void getPhotographer(final String id) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotographerService photoService = retrofit.create(PhotographerService.class);

        final Call<Photographer> call = photoService.getPhotographer(id);
        call.enqueue(new Callback<Photographer>() {
            @Override
            public void onResponse(Call<Photographer> call, Response<Photographer> response) {
                new ResponseAction<>(response, new ResponseAction_Default<Photographer>() {
                    @Override
                    public void error(Response<Photographer> response) {

                    }

                    @Override
                    public void badRequest(Response<Photographer> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<Photographer> response) {

                    }

                    @Override
                    public void ok(Response<Photographer> response) {
                        createOrUpdatePhotographer(response.body());
                    }

                    @Override
                    public void okCreated(Response<Photographer> response) {

                    }

                    @Override
                    public void unAuthorized(Response<Photographer> response) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Photographer> call, Throwable t) {

            }
        });
    }

    public void deletePhotographers() {
        realm.beginTransaction();
        photographerModel(realm).deletePhotographers();
        realm.commitTransaction();
    }

    public void deletePhotograper(final String id) {
        realm.beginTransaction();
        photographerModel(realm).deletePhotographer(id);
        realm.commitTransaction();
    }

    private void createOrUpdatePhotographers(final List<Photographer> photographers) {
        if (photographers == null)
            return;

        realm.beginTransaction();
        for (Photographer photographer : photographers) {
            photographerModel(realm).createOrUpdate(photographer);
        }
        realm.commitTransaction();
    }

    private void createOrUpdatePhotographer(final Photographer photographer) {
        if (photographer == null)
            return;

        realm.beginTransaction();
        photographerModel(realm).createOrUpdate(photographer);
        realm.commitTransaction();
    }
}
