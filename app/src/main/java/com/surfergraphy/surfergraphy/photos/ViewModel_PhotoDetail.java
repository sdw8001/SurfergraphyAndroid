package com.surfergraphy.surfergraphy.photos;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surfergraphy.base.viewmodel.BaseViewModel;
import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.photos.data.PhotoBuyHistory;
import com.surfergraphy.surfergraphy.photos.data.PhotoSaveHistory;
import com.surfergraphy.surfergraphy.photos.data.repositories.PhotoBuyHistoryRepository;
import com.surfergraphy.surfergraphy.photos.data.repositories.PhotoSaveHistoryRepository;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoBuyHistory;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoSaveHistory;

public class ViewModel_PhotoDetail extends BaseViewModel {

    private PhotoSaveHistoryRepository photoSaveHistoryRepository;
    private PhotoBuyHistoryRepository photoBuyHistoryRepository;
    private LiveData<AccessToken> accessTokenLiveData;
    private LiveData<Photo> photoListLiveData;
    private LiveData<PhotoSaveHistory> photoSaveHistoryLiveData;
    private LiveData<PhotoBuyHistory> photoBuyHistoryLiveData;

    public ViewModel_PhotoDetail(Application application) {
        super(application);
        photoSaveHistoryRepository = new PhotoSaveHistoryRepository(realm);
        photoBuyHistoryRepository = new PhotoBuyHistoryRepository(realm);
    }

    public LiveData<Photo> getPhoto(int photoId) {
        LiveRealmData<Photo> photoLiveRealmData = photoModel(realm).findPhotoLiveData(photoId);
        photoListLiveData = Transformations.map(photoLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return photoListLiveData;
    }

    public LiveData<AccessToken> getAccessToken() {
        LiveRealmData<AccessToken> liveRealmData = accessTokenModel(realm).findAccessToken();
        accessTokenLiveData = Transformations.map(liveRealmData, data -> data.size() > 0 ? data.get(0) : null);
        return accessTokenLiveData;
    }

    public LiveData<PhotoSaveHistory> getUserPhotoSaveHistory(final String userId, final int photoId) {
        LiveRealmData<PhotoSaveHistory> liveRealmData = photoSaveHistory(realm).findPhotoSaveHistoryLiveData(userId, photoId);
        photoSaveHistoryLiveData = Transformations.map(liveRealmData, data -> data.size() > 0 ? data.get(0) : null);
        return photoSaveHistoryLiveData;
    }

    public LiveData<PhotoBuyHistory> getUserPhotoBuyHistory(final String userId, final int photoId) {
        LiveRealmData<PhotoBuyHistory> liveRealmData = photoBuyHistory(realm).findPhotoBuyHistoryLiveData(userId, photoId);
        photoBuyHistoryLiveData = Transformations.map(liveRealmData, data -> data.size() > 0 ? data.get(0) : null);
        return photoBuyHistoryLiveData;
    }

    public void savePhoto(final int actionCode, final String userId, final int photoId) {
        photoSaveHistoryRepository.savePhoto(actionCode, userId, photoId);
    }

    public void buyPhoto(final int actionCode, final String userId, final int photoId) {
        if (photoSaveHistory(realm).findPhotoSaveHistory(photoId) == null) {
            photoSaveHistoryRepository.savePhoto(actionCode, userId, photoId);
        }
        photoBuyHistoryRepository.buyPhoto(actionCode, userId, photoId);
    }
}
