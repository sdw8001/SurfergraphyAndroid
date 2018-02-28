package com.surfergraphy.surf.like.data;

import com.surfergraphy.surf.photos.data.Photo;
import com.surfergraphy.surf.photos.data.PhotoBuyHistory;
import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surf.utils.RealmUtils.asLiveData;

public class LikePhotoDao {

    private Realm realm;

    public LikePhotoDao(Realm realm) {
        this.realm = realm;
    }

    public LikePhoto createOrUpdate(LikePhoto likePhoto) {
        if (likePhoto != null) {
            likePhoto.photo = realm.where(Photo.class).equalTo("id", likePhoto.photoId).findFirst();
            likePhoto.photoBuyHistory = realm.where(PhotoBuyHistory.class).equalTo("id", likePhoto.photoBuyHistoryId).findFirst();
            likePhoto = realm.copyToRealmOrUpdate(likePhoto);
        }
        return likePhoto;
    }

    public LiveRealmData<LikePhoto> findLikePhotosLiveData() {
        return asLiveData(realm.where(LikePhoto.class).findAllAsync());
    }

    public LiveRealmData<LikePhoto> findLikePhotosLiveData(final String userId) {
        return asLiveData(realm.where(LikePhoto.class).equalTo("userId", userId).findAllAsync());
    }

    public LiveRealmData<LikePhoto> findLikePhotoLiveData(final String userId, final int photoId) {
        return asLiveData(realm.where(LikePhoto.class).equalTo("userId", userId).equalTo("photoId", photoId).findAllAsync());
    }

    public LikePhoto findLikePhoto(final int photoId) {
        return realm.where(LikePhoto.class).equalTo("photoId", photoId).findFirst();
    }

    public LikePhoto findLikePhoto(final String userId, final int photoId) {
        return realm.where(LikePhoto.class).equalTo("userId", userId).equalTo("photoId", photoId).findFirst();
    }

    public void deleteLikePhotos() {
        realm.delete(LikePhoto.class);
    }

    public void deleteLikePhoto(final int id) {
        realm.where(LikePhoto.class).equalTo("id", id).findAll().deleteAllFromRealm();
    }

}
