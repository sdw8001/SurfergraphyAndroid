package com.surfergraphy.surfergraphy.photos.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surfergraphy.base.ActionCode;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.data.repositories.BaseRepository;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surfergraphy.photos.data.PhotoSaveHistory;
import com.surfergraphy.surfergraphy.photos.data.api.PhotoSaveHistoryService;
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

import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoSaveHistory;

public class PhotoSaveHistoryRepository extends BaseRepository {

    public PhotoSaveHistoryRepository(Realm realm) {
        super(realm);
        syncDataUserPhotos();
    }

    public void syncDataUserPhotos() {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotoSaveHistoryService photoSaveHistoryService = retrofit.create(PhotoSaveHistoryService.class);

        final Call<List<PhotoSaveHistory>> call = photoSaveHistoryService.getUserPhotoSaveHistories();
        call.enqueue(new Callback<List<PhotoSaveHistory>>() {
            @Override
            public void onResponse(Call<List<PhotoSaveHistory>> call, Response<List<PhotoSaveHistory>> response) {
                new ResponseAction<>(response, new ResponseAction_Default<List<PhotoSaveHistory>>() {
                    @Override
                    public void error(Response<List<PhotoSaveHistory>> response) {

                    }

                    @Override
                    public void badRequest(Response<List<PhotoSaveHistory>> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<List<PhotoSaveHistory>> response) {

                    }

                    @Override
                    public void ok(Response<List<PhotoSaveHistory>> response) {
                        deletePhotoSaveHistories();
                        createOrUpdatePhotoSaveHistories(response.body());
                    }

                    @Override
                    public void okCreated(Response<List<PhotoSaveHistory>> response) {

                    }

                    @Override
                    public void unAuthorized(Response<List<PhotoSaveHistory>> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<PhotoSaveHistory>> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    public void getPhoto(final int actionCode, final int photoId) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotoSaveHistoryService photoSaveHistoryService = retrofit.create(PhotoSaveHistoryService.class);

        final Call<List<PhotoSaveHistory>> call = photoSaveHistoryService.getPhotoSaveHistoryByPhoto(photoId);
        call.enqueue(new Callback<List<PhotoSaveHistory>>() {
            @Override
            public void onResponse(Call<List<PhotoSaveHistory>> call, Response<List<PhotoSaveHistory>> response) {
                new ResponseAction<>(response, new ResponseAction_Default<List<PhotoSaveHistory>>() {
                    @Override
                    public void error(Response<List<PhotoSaveHistory>> response) {

                    }

                    @Override
                    public void badRequest(Response<List<PhotoSaveHistory>> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<List<PhotoSaveHistory>> response) {

                    }

                    @Override
                    public void ok(Response<List<PhotoSaveHistory>> response) {
                        createOrUpdatePhotoSaveHistories(response.body());
                    }

                    @Override
                    public void okCreated(Response<List<PhotoSaveHistory>> response) {

                    }

                    @Override
                    public void unAuthorized(Response<List<PhotoSaveHistory>> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<PhotoSaveHistory>> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    public void savePhoto(final int actionCode, final String userId, final int photoId) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotoSaveHistoryService photoSaveHistoryService = retrofit.create(PhotoSaveHistoryService.class);

        final Call<PhotoSaveHistory> call = photoSaveHistoryService.addPhotoSaveHistory(userId, photoId);
        call.enqueue(new Callback<PhotoSaveHistory>() {
            @Override
            public void onResponse(Call<PhotoSaveHistory> call, Response<PhotoSaveHistory> response) {
                new ResponseAction<>(response, new ResponseAction_Default<PhotoSaveHistory>() {
                    @Override
                    public void error(Response<PhotoSaveHistory> response) {

                    }

                    @Override
                    public void badRequest(Response<PhotoSaveHistory> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<PhotoSaveHistory> response) {

                    }

                    @Override
                    public void ok(Response<PhotoSaveHistory> response) {

                    }

                    @Override
                    public void okCreated(Response<PhotoSaveHistory> response) {

                        if (actionCode == ActionCode.ACTION_PHOTO_DETAIL_SAVE) {
                            ActionResponse actionResponse = new ActionResponse();
                            actionResponse.setActionCode(actionCode);
                            actionResponse.setResultCode(response.code());
                            actionResponse.setMessage("저장되었습니다.");
                            createOrUpdateActionResponse(actionResponse);
                        }

                        createOrUpdatePhotoSaveHistory(response.body());
                    }

                    @Override
                    public void unAuthorized(Response<PhotoSaveHistory> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<PhotoSaveHistory> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    private void createOrUpdatePhotoSaveHistory(final PhotoSaveHistory photoSaveHistory) {
        if (photoSaveHistory == null)
            return;

        realm.beginTransaction();
        photoSaveHistory(realm).createOrUpdate(photoSaveHistory);
        realm.commitTransaction();
    }

    private void createOrUpdatePhotoSaveHistories(final List<PhotoSaveHistory> photoSaveHistories) {
        if (photoSaveHistories == null)
            return;

        realm.beginTransaction();
        for (PhotoSaveHistory photoSaveHistory : photoSaveHistories) {
            photoSaveHistory(realm).createOrUpdate(photoSaveHistory);
        }
        realm.commitTransaction();
    }

    public void deletePhotoSaveHistories() {

        realm.beginTransaction();
        photoSaveHistory(realm).deletePhotoSaveHistories();
        realm.commitTransaction();
    }
}