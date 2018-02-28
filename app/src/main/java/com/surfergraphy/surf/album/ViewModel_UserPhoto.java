package com.surfergraphy.surf.album;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surf.album.data.UserPhoto;
import com.surfergraphy.surf.album.data.repositories.UserPhotoRepository;
import com.surfergraphy.surf.base.viewmodel.BaseViewModel;
import com.surfergraphy.surf.login.data.AccessToken;
import com.surfergraphy.surf.photos.data.repositories.PhotoBuyHistoryRepository;
import com.surfergraphy.surf.photos.data.repositories.PhotoSaveHistoryRepository;
import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.RealmResults;

import static com.surfergraphy.surf.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surf.utils.RealmUtils.userPhotoModel;

public class ViewModel_UserPhoto extends BaseViewModel {

    private UserPhotoRepository userPhotoRepository;
    private PhotoSaveHistoryRepository photoSaveHistoryRepository;
    private PhotoBuyHistoryRepository photoBuyHistoryRepository;
    private LiveData<AccessToken> accessTokenLiveData;
    private LiveData<RealmResults<UserPhoto>> userPhotoListLiveData;

    public ViewModel_UserPhoto(Application application) {
        super(application);
        userPhotoRepository = UserPhotoRepository.getInstance(realm);
        photoSaveHistoryRepository = PhotoSaveHistoryRepository.getInstance(realm);
        photoBuyHistoryRepository = PhotoBuyHistoryRepository.getInstance(realm);
    }

    public void syncDataUserPhotos() {
        photoSaveHistoryRepository.syncDataUserPhotos();
        photoBuyHistoryRepository.syncDataUserPhotos();
        userPhotoRepository.syncDataUserPhotos();
    }

    public LiveData<RealmResults<UserPhoto>> getUserPhotos() {
        userPhotoListLiveData = userPhotoModel(realm).findUserPhotoLiveData(getAccountUser().id);
        return userPhotoListLiveData;
    }

    public LiveData<AccessToken> getAccessToken() {
        LiveRealmData<AccessToken> accessTokenLiveRealmData = accessTokenModel(realm).findAccessToken();
        accessTokenLiveData = Transformations.map(accessTokenLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return accessTokenLiveData;
    }
}
