package com.surfergraphy.surf.photos.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surf.album.data.repositories.UserPhotoRepository;
import com.surfergraphy.surf.base.ActionCode;
import com.surfergraphy.surf.base.data.ActionResponse;
import com.surfergraphy.surf.base.data.repositories.BaseRepository;
import com.surfergraphy.surf.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surf.login.data.LoginMember;
import com.surfergraphy.surf.photos.data.PhotoSaveHistory;
import com.surfergraphy.surf.photos.data.api.PhotoSaveHistoryService;
import com.surfergraphy.surf.utils.DateDeserializer;
import com.surfergraphy.surf.utils.ResponseAction;
import com.surfergraphy.surf.utils.RetrofitAdapter;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.surfergraphy.surf.utils.RealmUtils.photoSaveHistoryModel;

public class PhotoSaveHistoryRepository extends BaseRepository {

    private static PhotoSaveHistoryRepository instance;

    public static PhotoSaveHistoryRepository getInstance(Realm realm) {
        if (instance == null)
            instance = new PhotoSaveHistoryRepository(realm);
        return instance;
    }

    private PhotoSaveHistoryRepository(Realm realm) {
        super(realm);
        syncDataUserPhotos();
    }

    public void syncDataUserPhotos() {
        LoginMember currentLoginMember = realm.where(LoginMember.class).findFirst();
        if (currentLoginMember != null) {
            Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
            Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
            PhotoSaveHistoryService photoSaveHistoryService = retrofit.create(PhotoSaveHistoryService.class);

            final Call<List<PhotoSaveHistory>> call = photoSaveHistoryService.getUserPhotoSaveHistories(currentLoginMember.Id);
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
                            updateExpiredLoginMember(true);
                        }
                    });

                }

                @Override
                public void onFailure(Call<List<PhotoSaveHistory>> call, Throwable t) {
                    new Throwable("getPhoto Failed", t);
                }
            });
        }
    }

    public void savePhoto(final int actionCode, final String userId, final int photoId, final int photoBuyHistoryId) {
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

                        if (actionCode == ActionCode.ACTION_CREATE_PHOTO_DETAIL_SAVE) {
                            ActionResponse actionResponse = new ActionResponse();
                            actionResponse.setActionCode(actionCode);
                            actionResponse.setResultCode(response.code());
                            actionResponse.setMessage("저장되었습니다.");
                            createOrUpdateActionResponse(actionResponse);
                        }

                        PhotoSaveHistory photoSaveHistory = createOrUpdatePhotoSaveHistory(response.body());
                        if (photoSaveHistory != null) {
                            UserPhotoRepository.getInstance(realm).saveUserPhoto(actionCode, userId, photoId, photoSaveHistory.id, photoBuyHistoryId);
                        }
                    }

                    @Override
                    public void unAuthorized(Response<PhotoSaveHistory> response) {
                        updateExpiredLoginMember(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<PhotoSaveHistory> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    private PhotoSaveHistory createOrUpdatePhotoSaveHistory(final PhotoSaveHistory photoSaveHistory) {
        if (photoSaveHistory == null)
            return null;

        realm.beginTransaction();
        photoSaveHistoryModel(realm).createOrUpdate(photoSaveHistory);
        realm.commitTransaction();
        return photoSaveHistory;
    }

    private void createOrUpdatePhotoSaveHistories(final List<PhotoSaveHistory> photoSaveHistories) {
        if (photoSaveHistories == null)
            return;

        realm.beginTransaction();
        for (PhotoSaveHistory photoSaveHistory : photoSaveHistories) {
            photoSaveHistoryModel(realm).createOrUpdate(photoSaveHistory);
        }
        realm.commitTransaction();
    }

    public void deletePhotoSaveHistories() {

        realm.beginTransaction();
        photoSaveHistoryModel(realm).deletePhotoSaveHistories();
        realm.commitTransaction();
    }
}