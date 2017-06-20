package com.surfergraphy.surfergraphy.photos.data;

import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.asLiveData;

public class PhotoDao {

    private Realm realm;

    public PhotoDao(Realm realm) {
        this.realm = realm;
    }

    public Photo createOrUpdate(Photo photo) {
        if (photo != null) {
            photo = realm.copyToRealmOrUpdate(photo);
        }
        return photo;
    }

    public void addPhoto(final int id, final String name, final String url, final String place, final String mimeType) {
        Photo photo = new Photo(id, name, url, place, mimeType);
        realm.insert(photo);
    }

    public LiveRealmData<Photo> findPhotos() {
        return asLiveData(realm.where(Photo.class).findAllAsync());
    }
}
