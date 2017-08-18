package com.surfergraphy.surfergraphy.album.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surfergraphy.album.data.UserPhoto;
import com.surfergraphy.surfergraphy.album.data.api.UserPhotoService;
import com.surfergraphy.surfergraphy.base.ActionCode;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.data.repositories.BaseRepository;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.photos.data.PhotoBuyHistory;
import com.surfergraphy.surfergraphy.photos.data.PhotoSaveHistory;
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
import static com.surfergraphy.surfergraphy.utils.RealmUtils.userPhotoModel;

public class UserPhotoRepository extends BaseRepository {

    private static UserPhotoRepository instance;
    public static UserPhotoRepository getInstance(Realm realm) {
        if (instance == null)
            instance = new UserPhotoRepository(realm);
        return instance;
    }

    private UserPhotoRepository(Realm realm) {
        super(realm);
        syncDataUserPhotos();
    }

    public void syncDataUserPhotos() {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        UserPhotoService userPhotoService = retrofit.create(UserPhotoService.class);

        final Call<List<UserPhoto>> call = userPhotoService.getUserPhotos();
        call.enqueue(new Callback<List<UserPhoto>>() {
            @Override
            public void onResponse(Call<List<UserPhoto>> call, Response<List<UserPhoto>> response) {
                new ResponseAction<>(response, new ResponseAction_Default<List<UserPhoto>>() {
                    @Override
                    public void error(Response<List<UserPhoto>> response) {

                    }

                    @Override
                    public void badRequest(Response<List<UserPhoto>> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<List<UserPhoto>> response) {

                    }

                    @Override
                    public void ok(Response<List<UserPhoto>> response) {
                        deleteUserPhotos();
                        createOrUpdateUserPhotos(response.body());
                    }

                    @Override
                    public void okCreated(Response<List<UserPhoto>> response) {

                    }

                    @Override
                    public void unAuthorized(Response<List<UserPhoto>> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<UserPhoto>> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    public void saveUserPhoto(final int actionCode, final String userId, final int photoId, final int photoSaveHistoryId, final int photoBuyHistoryId) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        UserPhotoService userPhotoService = retrofit.create(UserPhotoService.class);

        final Call<UserPhoto> call = userPhotoService.addUserPhoto(userId, photoId, photoSaveHistoryId, photoBuyHistoryId);
        call.enqueue(new Callback<UserPhoto>() {
            @Override
            public void onResponse(Call<UserPhoto> call, Response<UserPhoto> response) {
                new ResponseAction<>(response, new ResponseAction_Default<UserPhoto>() {
                    @Override
                    public void error(Response<UserPhoto> response) {

                    }

                    @Override
                    public void badRequest(Response<UserPhoto> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<UserPhoto> response) {

                    }

                    @Override
                    public void ok(Response<UserPhoto> response) {

                    }

                    @Override
                    public void okCreated(Response<UserPhoto> response) {

                        if (actionCode == ActionCode.ACTION_CREATE_USER_PHOTO) {
                            ActionResponse actionResponse = new ActionResponse();
                            actionResponse.setActionCode(actionCode);
                            actionResponse.setResultCode(response.code());
                            actionResponse.setMessage("저장되었습니다.");
                            createOrUpdateActionResponse(actionResponse);
                        }

                        createOrUpdateUserPhoto(response.body());
                    }

                    @Override
                    public void unAuthorized(Response<UserPhoto> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<UserPhoto> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    public void deleteUserPhotos() {
        realm.beginTransaction();
        userPhotoModel(realm).deletePhotos();
        realm.commitTransaction();
    }

    private void createOrUpdateUserPhoto(final UserPhoto userPhoto) {
        if (userPhoto == null)
            return;

        realm.beginTransaction();
        userPhotoModel(realm).createOrUpdate(userPhoto);
        realm.commitTransaction();

    }

    private void createOrUpdateUserPhotos(final List<UserPhoto> userPhotos) {
        if (userPhotos == null)
            return;

        realm.beginTransaction();
        for (UserPhoto userPhoto : userPhotos) {
            userPhotoModel(realm).createOrUpdate(userPhoto);
        }
        realm.commitTransaction();
    }
}
