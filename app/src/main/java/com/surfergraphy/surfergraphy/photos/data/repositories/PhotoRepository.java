package com.surfergraphy.surfergraphy.photos.data.repositories;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surfergraphy.utils.DateDeserializer;
import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.photos.data.api.PhotoService;
import com.surfergraphy.surfergraphy.utils.RetrofitAdapter;

import org.threeten.bp.LocalDateTime;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoModel;

public class PhotoRepository {
    private Realm realm;

    public void getPhotos(final Realm realm) {
        this.realm = realm;
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotoService photoService = retrofit.create(PhotoService.class);
        final Call<List<Photo>> call = photoService.getPhotos();
        new NetworkCall().execute(call);
    }

    private class NetworkCall extends AsyncTask<Call, Void, List<Photo>> {
        @Override
        protected List<Photo> doInBackground(Call[] params) {
            try {
                Call<List<Photo>> call = params[0];
                Response<List<Photo>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Photo> result) {
            createOrUpdatePhotos(result);
        }
    }

    private void createOrUpdatePhotos(final List<Photo> photos) {
        realm.beginTransaction();
        for (Photo photo : photos) {
            photoModel(realm).createOrUpdate(photo);
        }
        realm.commitTransaction();
    }
}
