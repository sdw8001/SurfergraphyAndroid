package com.surfergraphy.surfergraphy.photos.data;

import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.asLiveData;

public class PhotoSaveHistoryDao {

    private Realm realm;

    public PhotoSaveHistoryDao(Realm realm) {
        this.realm = realm;
    }

    public PhotoSaveHistory createOrUpdate(PhotoSaveHistory photoSaveHistory) {
        if (photoSaveHistory != null) {
            photoSaveHistory = realm.copyToRealmOrUpdate(photoSaveHistory);
        }
        return photoSaveHistory;
    }

    public LiveRealmData<PhotoSaveHistory> findPhotoSaveHistories() {
        return asLiveData(realm.where(PhotoSaveHistory.class).findAllAsync());
    }

    public void deletePhotoSaveHistories() {
        realm.delete(PhotoSaveHistory.class);
    }

}
