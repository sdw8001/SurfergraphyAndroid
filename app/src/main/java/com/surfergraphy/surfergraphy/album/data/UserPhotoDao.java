package com.surfergraphy.surfergraphy.album.data;

import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.photos.data.PhotoBuyHistory;
import com.surfergraphy.surfergraphy.photos.data.PhotoSaveHistory;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.asLiveData;

public class UserPhotoDao {

    private Realm realm;

    public UserPhotoDao(Realm realm) {
        this.realm = realm;
    }

    public UserPhoto createOrUpdate(UserPhoto userPhoto) {
        if (userPhoto != null) {
            userPhoto.photo = realm.where(Photo.class).equalTo("id", userPhoto.photoId).findFirst();
            userPhoto.photoSaveHistory = realm.where(PhotoSaveHistory.class).equalTo("id", userPhoto.photoSaveHistoryId).findFirst();
            userPhoto.photoBuyHistory = realm.where(PhotoBuyHistory.class).equalTo("id", userPhoto.photoBuyHistoryId).findFirst();
            userPhoto = realm.copyToRealmOrUpdate(userPhoto);
        }
        return userPhoto;
    }

    public void addUserPhoto(final int id, final String userId, final int photoId, final int photoSaveHistoryId, final int photoBuyHistoryId, final boolean deleted) {
        UserPhoto userPhoto = new UserPhoto(id, userId, photoId, photoSaveHistoryId, photoBuyHistoryId, deleted);
        realm.insert(userPhoto);
    }

    public LiveRealmData<UserPhoto> findUserPhotosLiveData() {
        return asLiveData(realm.where(UserPhoto.class).findAllAsync());
    }

    public LiveRealmData<UserPhoto> findUserPhotoLiveData(final String userId) {
        RealmResults<UserPhoto> realmResults = realm.where(UserPhoto.class).equalTo("userId", userId).findAllAsync();
        for (UserPhoto userPhoto : realmResults) {
            realm.executeTransaction(realm1 -> {
                userPhoto.photo = realm.where(Photo.class).equalTo("id", userPhoto.photoId).findFirst();
                userPhoto.photoSaveHistory = realm.where(PhotoSaveHistory.class).equalTo("id", userPhoto.photoSaveHistoryId).findFirst();
                userPhoto.photoBuyHistory = realm.where(PhotoBuyHistory.class).equalTo("id", userPhoto.photoBuyHistoryId).findFirst();
            });
        }
        return asLiveData(realm.where(UserPhoto.class).equalTo("userId", userId).findAllAsync());
    }

    public RealmResults<UserPhoto> findUserPhoto(int userPhotoId) {
        return realm.where(UserPhoto.class).equalTo("id", userPhotoId).findAllAsync();
    }

    public void deletePhotos() {
        realm.delete(UserPhoto.class);
    }
}
