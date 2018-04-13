package com.surfergraphy.surf.album;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.surfergraphy.surf.album.data.UserPhoto;
import com.surfergraphy.surf.album.data.repositories.UserPhotoRepository;
import com.surfergraphy.surf.base.viewmodel.BaseViewModel;
import com.surfergraphy.surf.photos.data.repositories.PhotoBuyHistoryRepository;
import com.surfergraphy.surf.photos.data.repositories.PhotoSaveHistoryRepository;

import io.realm.RealmResults;

import static com.surfergraphy.surf.utils.RealmUtils.userPhotoModel;

public class ViewModel_UserPhoto extends BaseViewModel {

    private UserPhotoRepository userPhotoRepository;
    private PhotoSaveHistoryRepository photoSaveHistoryRepository;
    private PhotoBuyHistoryRepository photoBuyHistoryRepository;
    private LiveData<RealmResults<UserPhoto>> userPhotoListLiveData;

    public ViewModel_UserPhoto(Application application) {
        super(application);
        userPhotoRepository = UserPhotoRepository.getInstance(realm);
        photoSaveHistoryRepository = PhotoSaveHistoryRepository.getInstance(realm);
        photoBuyHistoryRepository = PhotoBuyHistoryRepository.getInstance(realm);
    }

    public void syncDataUserPhotos() {
        photoSaveHistoryRepository.syncDataUserPhotos();
        photoBuyHistoryRepository.syncDataPhotoByHistories();
        userPhotoRepository.syncDataUserPhotos();
    }

    public LiveData<RealmResults<UserPhoto>> getUserPhotos() {
        userPhotoListLiveData = userPhotoModel(realm).findUserPhotoLiveData(getLoginMember().Id);
        return userPhotoListLiveData;
    }

    public String getUserPhotoUrl(final int id) {
        UserPhoto userPhoto = realm.where(UserPhoto.class).equalTo("id", id).findFirst();
        if (userPhoto != null) {
            return userPhoto.photo.url;
        }
        return null;
    }

    public void toggleSelectableUserPhoto(final String userId) {
        userPhotoRepository.toggleSelectableUserPhoto(userId);
    }
}
