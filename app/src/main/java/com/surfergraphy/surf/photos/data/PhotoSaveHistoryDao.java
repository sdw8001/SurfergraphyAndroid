package com.surfergraphy.surf.photos.data;

import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surf.utils.RealmUtils.asLiveData;

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

    public LiveRealmData<PhotoSaveHistory> findPhotoSaveHistoriesLiveData() {
        return asLiveData(realm.where(PhotoSaveHistory.class).findAllAsync());
    }

    public LiveRealmData<PhotoSaveHistory> findPhotoSaveHistoriesLiveData(final String userId) {
        return asLiveData(realm.where(PhotoSaveHistory.class).equalTo("userId", userId).findAllAsync());
    }

    public LiveRealmData<PhotoSaveHistory> findPhotoSaveHistoryLiveData(final String userId, final int photoId) {
        return asLiveData(realm.where(PhotoSaveHistory.class).equalTo("userId", userId).equalTo("photoId", photoId).findAllAsync());
    }

    public PhotoSaveHistory findPhotoSaveHistory(final int photoId) {
        return realm.where(PhotoSaveHistory.class).equalTo("photoId", photoId).findFirst();
    }

    public PhotoSaveHistory findPhotoSaveHistory(final String userId, final int photoId) {
        return realm.where(PhotoSaveHistory.class).equalTo("userId", userId).equalTo("photoId", photoId).findFirst();
    }

    public void deletePhotoSaveHistories() {
        realm.delete(PhotoSaveHistory.class);
    }

}
