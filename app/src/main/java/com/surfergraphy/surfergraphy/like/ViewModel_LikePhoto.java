package com.surfergraphy.surfergraphy.like;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surfergraphy.base.viewmodel.BaseViewModel;
import com.surfergraphy.surfergraphy.like.data.LikePhoto;
import com.surfergraphy.surfergraphy.like.data.repositories.LikePhotoRepository;
import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.photos.data.repositories.PhotoBuyHistoryRepository;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.RealmResults;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.likePhotoModel;

public class ViewModel_LikePhoto extends BaseViewModel {

    private LikePhotoRepository likePhotoRepository;
    private PhotoBuyHistoryRepository photoBuyHistoryRepository;
    private LiveData<AccessToken> accessTokenLiveData;
    private LiveData<LikePhoto> likePhotoLiveData;
    private LiveData<RealmResults<LikePhoto>> likePhotoListLiveData;

    public ViewModel_LikePhoto(Application application) {
        super(application);
        likePhotoRepository = LikePhotoRepository.getInstance(realm);
        photoBuyHistoryRepository = PhotoBuyHistoryRepository.getInstance(realm);
    }

    public void syncDataLikePhotos() {
        photoBuyHistoryRepository.syncDataUserPhotos();
        likePhotoRepository.syncDataUserPhotos();
    }

    public LiveData<RealmResults<LikePhoto>> getLikePhotos() {
        likePhotoListLiveData = likePhotoModel(realm).findLikePhotosLiveData(getAccountUser().id);
        return likePhotoListLiveData;
    }

    public LiveData<AccessToken> getAccessToken() {
        LiveRealmData<AccessToken> accessTokenLiveRealmData = accessTokenModel(realm).findAccessToken();
        accessTokenLiveData = Transformations.map(accessTokenLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return accessTokenLiveData;
    }

    public void likePhoto(final int actionCode, final String userId, final int photoId) {
        likePhotoRepository.likePhoto(actionCode, userId, photoId);
    }

    public void cancelLikePhoto(final int actionCode, final int id) {
        likePhotoRepository.cancelLikePhoto(actionCode, id);
    }

    public LiveData<LikePhoto> getLikePhoto(final String userId, final int photoId) {
        LiveRealmData<LikePhoto> liveRealmData = likePhotoModel(realm).findLikePhotoLiveData(userId, photoId);
        likePhotoLiveData = Transformations.map(liveRealmData, data -> data.size() > 0 ? data.get(0) : null);
        return likePhotoLiveData;
    }
}
