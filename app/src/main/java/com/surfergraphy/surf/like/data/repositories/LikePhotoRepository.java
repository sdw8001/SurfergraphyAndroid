package com.surfergraphy.surf.like.data.repositories;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surf.base.ActionCode;
import com.surfergraphy.surf.base.data.ActionResponse;
import com.surfergraphy.surf.base.data.repositories.BaseRepository;
import com.surfergraphy.surf.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surf.like.data.LikePhoto;
import com.surfergraphy.surf.like.data.api.LikePhotoService;
import com.surfergraphy.surf.login.data.LoginMember;
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

import static com.surfergraphy.surf.utils.RealmUtils.likePhotoModel;

public class LikePhotoRepository extends BaseRepository {

    private static LikePhotoRepository instance;

    public static LikePhotoRepository getInstance(Realm realm) {
        if (instance == null)
            instance = new LikePhotoRepository(realm);
        return instance;
    }

    private LikePhotoRepository(Realm realm) {
        super(realm);
        syncDataUserPhotos();
    }

    public void syncDataUserPhotos() {
        LoginMember currentLoginMember = realm.where(LoginMember.class).findFirst();
        if (currentLoginMember != null) {
            Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
            Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
            LikePhotoService likePhotoService = retrofit.create(LikePhotoService.class);

            final Call<List<LikePhoto>> call = likePhotoService.getUserLikePhotos(currentLoginMember.Id);
            call.enqueue(new Callback<List<LikePhoto>>() {
                @Override
                public void onResponse(Call<List<LikePhoto>> call, Response<List<LikePhoto>> response) {
                    new ResponseAction<>(response, new ResponseAction_Default<List<LikePhoto>>() {
                        @Override
                        public void error(Response<List<LikePhoto>> response) {

                        }

                        @Override
                        public void badRequest(Response<List<LikePhoto>> response, ActionResponse actionResponse) {

                        }

                        @Override
                        public void notFound(Response<List<LikePhoto>> response) {

                        }

                        @Override
                        public void ok(Response<List<LikePhoto>> response) {
                            deleteLikePhotos();
                            createOrUpdateLikePhotos(response.body());
                        }

                        @Override
                        public void okCreated(Response<List<LikePhoto>> response) {

                        }

                        @Override
                        public void unAuthorized(Response<List<LikePhoto>> response) {
                            updateExpiredLoginMember(true);
                        }
                    });

                }

                @Override
                public void onFailure(Call<List<LikePhoto>> call, Throwable t) {
                    new Throwable("getPhoto Failed", t);
                }
            });
        }
    }

    public void likePhoto(final int actionCode, final String userId, final int photoId) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        LikePhotoService likePhotoService = retrofit.create(LikePhotoService.class);

        final Call<LikePhoto> call = likePhotoService.likePhoto(userId, photoId);
        call.enqueue(new Callback<LikePhoto>() {
            @Override
            public void onResponse(Call<LikePhoto> call, Response<LikePhoto> response) {
                new ResponseAction<>(response, new ResponseAction_Default<LikePhoto>() {
                    @Override
                    public void error(Response<LikePhoto> response) {

                    }

                    @Override
                    public void badRequest(Response<LikePhoto> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<LikePhoto> response) {

                    }

                    @Override
                    public void ok(Response<LikePhoto> response) {

                    }

                    @Override
                    public void okCreated(Response<LikePhoto> response) {

                        if (actionCode == ActionCode.ACTION_LIKE_PHOTO) {
                            ActionResponse actionResponse = new ActionResponse();
                            actionResponse.setActionCode(actionCode);
                            actionResponse.setResultCode(response.code());
                            actionResponse.setMessage("좋아요를 눌렀습니다.");
                            createOrUpdateLikePhoto(response.body());
                        }
                    }

                    @Override
                    public void unAuthorized(Response<LikePhoto> response) {
                        updateExpiredLoginMember(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<LikePhoto> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    public void cancelLikePhoto(final int actionCode, final int id) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        LikePhotoService likePhotoService = retrofit.create(LikePhotoService.class);

        final Call<LikePhoto> call = likePhotoService.cancelLikePhoto(id);
        call.enqueue(new Callback<LikePhoto>() {
            @Override
            public void onResponse(Call<LikePhoto> call, Response<LikePhoto> response) {
                new ResponseAction<>(response, new ResponseAction_Default<LikePhoto>() {
                    @Override
                    public void error(Response<LikePhoto> response) {
                        Log.e("error", response.message());
                    }

                    @Override
                    public void badRequest(Response<LikePhoto> response, ActionResponse actionResponse) {
                        Log.e("error", response.message());
                    }

                    @Override
                    public void notFound(Response<LikePhoto> response) {
                        Log.e("error", response.message());
                    }

                    @Override
                    public void ok(Response<LikePhoto> response) {
                        Log.e("error", response.message());
                    }

                    @Override
                    public void okCreated(Response<LikePhoto> response) {

                        if (actionCode == ActionCode.ACTION_LIKE_PHOTO_CANCEL) {
                            ActionResponse actionResponse = new ActionResponse();
                            actionResponse.setActionCode(actionCode);
                            actionResponse.setResultCode(response.code());
                            actionResponse.setMessage("좋아요를 취소했습니다.");
                            deleteLikePhotos(response.body());
                        }
                    }

                    @Override
                    public void unAuthorized(Response<LikePhoto> response) {
                        updateExpiredLoginMember(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<LikePhoto> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
    }

    private LikePhoto createOrUpdateLikePhoto(final LikePhoto likePhoto) {
        if (likePhoto == null)
            return null;

        realm.beginTransaction();
        likePhotoModel(realm).createOrUpdate(likePhoto);
        realm.commitTransaction();
        return likePhoto;
    }

    private void createOrUpdateLikePhotos(final List<LikePhoto> photoSaveHistories) {
        if (photoSaveHistories == null)
            return;

        realm.beginTransaction();
        for (LikePhoto likePhoto : photoSaveHistories) {
            likePhotoModel(realm).createOrUpdate(likePhoto);
        }
        realm.commitTransaction();
    }

    public void deleteLikePhotos() {

        realm.beginTransaction();
        likePhotoModel(realm).deleteLikePhotos();
        realm.commitTransaction();
    }

    public void deleteLikePhotos(final LikePhoto likePhoto) {
        if (likePhoto == null)
            return;

        realm.beginTransaction();
        likePhotoModel(realm).deleteLikePhoto(likePhoto.id);
        realm.commitTransaction();
    }
}