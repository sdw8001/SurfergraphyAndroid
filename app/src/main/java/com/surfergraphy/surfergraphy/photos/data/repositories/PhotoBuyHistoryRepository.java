package com.surfergraphy.surfergraphy.photos.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surfergraphy.album.data.repositories.UserPhotoRepository;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.data.repositories.BaseRepository;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surfergraphy.photos.data.PhotoBuyHistory;
import com.surfergraphy.surfergraphy.photos.data.PhotoSaveHistory;
import com.surfergraphy.surfergraphy.photos.data.api.PhotoBuyHistoryService;
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

import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoBuyHistoryModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoSaveHistoryModel;

public class PhotoBuyHistoryRepository extends BaseRepository {

    private static PhotoBuyHistoryRepository instance;
    public static PhotoBuyHistoryRepository getInstance(Realm realm) {
        if (instance == null)
            instance = new PhotoBuyHistoryRepository(realm);
        return instance;
    }

    private PhotoBuyHistoryRepository(Realm realm) {
        super(realm);
        syncDataUserPhotos();
    }

    public void syncDataUserPhotos() {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotoBuyHistoryService photoBuyHistoryService = retrofit.create(PhotoBuyHistoryService.class);

        final Call<List<PhotoBuyHistory>> call = photoBuyHistoryService.getUserPhotoBuyHistories();
        call.enqueue(new Callback<List<PhotoBuyHistory>>() {
            @Override
            public void onResponse(Call<List<PhotoBuyHistory>> call, Response<List<PhotoBuyHistory>> response) {
                new ResponseAction<>(response, new ResponseAction_Default<List<PhotoBuyHistory>>() {
                    @Override
                    public void error(Response<List<PhotoBuyHistory>> response) {

                    }

                    @Override
                    public void badRequest(Response<List<PhotoBuyHistory>> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<List<PhotoBuyHistory>> response) {

                    }

                    @Override
                    public void ok(Response<List<PhotoBuyHistory>> response) {
                        deletePhotoBuyHistories();
                        createOrUpdatePhotoBuyHistories(response.body());
                    }

                    @Override
                    public void okCreated(Response<List<PhotoBuyHistory>> response) {

                    }

                    @Override
                    public void unAuthorized(Response<List<PhotoBuyHistory>> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<PhotoBuyHistory>> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    public void getPhoto(final int actionCode, final int photoId) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotoBuyHistoryService photoBuyHistoryService = retrofit.create(PhotoBuyHistoryService.class);

        final Call<List<PhotoBuyHistory>> call = photoBuyHistoryService.getPhotoBuyHistoryByPhoto(photoId);
        call.enqueue(new Callback<List<PhotoBuyHistory>>() {
            @Override
            public void onResponse(Call<List<PhotoBuyHistory>> call, Response<List<PhotoBuyHistory>> response) {
                new ResponseAction<>(response, new ResponseAction_Default<List<PhotoBuyHistory>>() {
                    @Override
                    public void error(Response<List<PhotoBuyHistory>> response) {

                    }

                    @Override
                    public void badRequest(Response<List<PhotoBuyHistory>> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<List<PhotoBuyHistory>> response) {

                    }

                    @Override
                    public void ok(Response<List<PhotoBuyHistory>> response) {
                        createOrUpdatePhotoBuyHistories(response.body());
                    }

                    @Override
                    public void okCreated(Response<List<PhotoBuyHistory>> response) {

                    }

                    @Override
                    public void unAuthorized(Response<List<PhotoBuyHistory>> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<PhotoBuyHistory>> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    public void buyPhoto(final int actionCode, final String userId, final int photoId) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PhotoBuyHistoryService photobuyHistoryService = retrofit.create(PhotoBuyHistoryService.class);

        final Call<PhotoBuyHistory> call = photobuyHistoryService.addPhotoBuyHistory(userId, photoId);
        call.enqueue(new Callback<PhotoBuyHistory>() {
            @Override
            public void onResponse(Call<PhotoBuyHistory> call, Response<PhotoBuyHistory> response) {
                new ResponseAction<>(response, new ResponseAction_Default<PhotoBuyHistory>() {
                    @Override
                    public void error(Response<PhotoBuyHistory> response) {

                    }

                    @Override
                    public void badRequest(Response<PhotoBuyHistory> response, ActionResponse actionResponse) {
                        actionResponse.setActionCode(actionCode);
                        createOrUpdateActionResponse(actionResponse);
                    }

                    @Override
                    public void notFound(Response<PhotoBuyHistory> response) {

                    }

                    @Override
                    public void ok(Response<PhotoBuyHistory> response) {

                    }

                    @Override
                    public void okCreated(Response<PhotoBuyHistory> response) {
                        ActionResponse actionResponse = new ActionResponse();
                        actionResponse.setActionCode(actionCode);
                        actionResponse.setResultCode(response.code());
                        actionResponse.setMessage("구매되었습니다.");
                        createOrUpdateActionResponse(actionResponse);

                        syncAuthorizationAccountUser();

                        PhotoBuyHistory photoBuyHistory = createOrUpdatePhotoBuyHistory(response.body());
                        if (photoBuyHistory != null) {
                            PhotoSaveHistory photoSaveHistory = photoSaveHistoryModel(realm).findPhotoSaveHistory(userId, photoId);
                            if (photoSaveHistory != null) {
                                UserPhotoRepository.getInstance(realm).saveUserPhoto(actionCode, userId, photoId, photoSaveHistory.id, photoBuyHistory.id);
                            } else {
                                PhotoSaveHistoryRepository.getInstance(realm).savePhoto(actionCode, userId, photoId, photoBuyHistory.id);
                            }
                        }
                    }

                    @Override
                    public void unAuthorized(Response<PhotoBuyHistory> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<PhotoBuyHistory> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    private PhotoBuyHistory createOrUpdatePhotoBuyHistory(final PhotoBuyHistory photoBuyHistory) {
        if (photoBuyHistory == null)
            return null;

        realm.beginTransaction();
        photoBuyHistoryModel(realm).createOrUpdate(photoBuyHistory);
        realm.commitTransaction();
        return photoBuyHistory;
    }

    private void createOrUpdatePhotoBuyHistories(final List<PhotoBuyHistory> photoBuyHistories) {
        if (photoBuyHistories == null)
            return;

        realm.beginTransaction();
        for (PhotoBuyHistory photoBuyHistory : photoBuyHistories) {
            photoBuyHistoryModel(realm).createOrUpdate(photoBuyHistory);
        }
        realm.commitTransaction();
    }

    public void deletePhotoBuyHistories() {
        realm.beginTransaction();
        photoBuyHistoryModel(realm).deletePhotoBuyHistories();
        realm.commitTransaction();
    }
}