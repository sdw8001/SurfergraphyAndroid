package com.surfergraphy.surf.photos.data;

import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surf.utils.RealmUtils.asLiveData;

public class PhotoBuyHistoryDao {

    private Realm realm;

    public PhotoBuyHistoryDao(Realm realm) {
        this.realm = realm;
    }

    public PhotoBuyHistory createOrUpdate(PhotoBuyHistory photoBuyHistory) {
        if (photoBuyHistory != null) {
            photoBuyHistory = realm.copyToRealmOrUpdate(photoBuyHistory);
        }
        return photoBuyHistory;
    }

    public LiveRealmData<PhotoBuyHistory> findPhotoBuyHistoriesLiveData() {
        return asLiveData(realm.where(PhotoBuyHistory.class).findAllAsync());
    }

    public LiveRealmData<PhotoBuyHistory> findPhotoBuyHistoriesLiveData(final String userId) {
        return asLiveData(realm.where(PhotoBuyHistory.class).equalTo("userId", userId).findAllAsync());
    }

    public LiveRealmData<PhotoBuyHistory> findPhotoBuyHistoryLiveData(final int photoId) {
        return asLiveData(realm.where(PhotoBuyHistory.class).equalTo("photoId", photoId).findAllAsync());
    }

    public LiveRealmData<PhotoBuyHistory> findPhotoBuyHistoryLiveData(final String userId, final int photoId) {
        return asLiveData(realm.where(PhotoBuyHistory.class).equalTo("userId", userId).equalTo("photoId", photoId).findAllAsync());
    }

    public PhotoBuyHistory findPhotoBuyHistory(final int photoId) {
        return realm.where(PhotoBuyHistory.class).equalTo("photoId", photoId).findFirst();
    }

    public void deletePhotoBuyHistories() {
        realm.delete(PhotoBuyHistory.class);
    }

}
