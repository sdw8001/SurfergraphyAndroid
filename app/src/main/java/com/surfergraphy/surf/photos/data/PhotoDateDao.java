package com.surfergraphy.surf.photos.data;

import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surf.utils.RealmUtils.asLiveData;

public class PhotoDateDao {

    private Realm realm;

    public PhotoDateDao(Realm realm) {
        this.realm = realm;
    }

    public PhotoDate createOrUpdate(PhotoDate photoDate) {
        if (photoDate != null) {
            photoDate = realm.copyToRealmOrUpdate(photoDate);
        }
        return photoDate;
    }

    public LiveRealmData<PhotoDate> findPhotoDateLiveData() {
        return asLiveData(realm.where(PhotoDate.class).findAllAsync());
    }

    public void deleteDatePhotos() {
        realm.delete(PhotoDate.class);
    }
}
