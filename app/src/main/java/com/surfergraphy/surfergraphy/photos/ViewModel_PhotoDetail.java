package com.surfergraphy.surfergraphy.photos;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surfergraphy.base.viewmodel.BaseViewModel;
import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.photos.data.repositories.PhotoRepository;
import com.surfergraphy.surfergraphy.photos.data.repositories.PhotoSaveHistoryRepository;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoModel;

public class ViewModel_PhotoDetail extends BaseViewModel {

    private PhotoSaveHistoryRepository photoSaveHistoryRepository;
    private LiveData<AccessToken> accessTokenLiveData;
    private LiveData<Photo> photoListLiveData;

    public ViewModel_PhotoDetail(Application application) {
        super(application);
        photoSaveHistoryRepository = new PhotoSaveHistoryRepository();
    }

    public LiveData<Photo> getPhoto(int photoId) {
        LiveRealmData<Photo> photoLiveRealmData = photoModel(realm).findPhotos(photoId);
        photoListLiveData = Transformations.map(photoLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return photoListLiveData;
    }

    public LiveData<AccessToken> getAccessToken() {
        LiveRealmData<AccessToken> accessTokenLiveRealmData = accessTokenModel(realm).findAccessToken();
        accessTokenLiveData = Transformations.map(accessTokenLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return accessTokenLiveData;
    }

    public void savePhoto(final String userId, final int photoId) {
        photoSaveHistoryRepository.savePhoto(realm, userId, photoId);
    }

    public void buyPhoto(int photoId) {

    }
}
