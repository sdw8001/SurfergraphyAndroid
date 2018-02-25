package com.surfergraphy.surfergraphy.photos.data;

import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.asLiveData;

public class PhotographerDao {

    private Realm realm;

    public PhotographerDao(Realm realm) {
        this.realm = realm;
    }

    public Photographer createOrUpdate(Photographer photo) {
        if (photo != null) {
            photo = realm.copyToRealmOrUpdate(photo);
        }
        return photo;
    }

    public LiveRealmData<Photographer> findPhotographersLiveData() {
        return asLiveData(realm.where(Photographer.class).findAllAsync());
    }

    public LiveRealmData<Photographer> findPhotographerLiveData(String photographerId) {
        return asLiveData(realm.where(Photographer.class).equalTo("id", photographerId).findAllAsync());
    }

    public RealmResults<Photographer> findPhotographer(int photographerId) {
        return realm.where(Photographer.class).equalTo("id", photographerId).findAllAsync();
    }

    public void deletePhotographers() {
        realm.delete(Photographer.class);
    }
    public void deletePhotographer(String photographerId) {
        realm.where(Photographer.class).equalTo("id", photographerId).findAll().deleteAllFromRealm();
    }
}
