package com.surfergraphy.surfergraphy.photos;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.text.TextUtils;

import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.photos.data.ViewInfo_Photo;
import com.surfergraphy.surfergraphy.photos.data.repositories.PhotoRepository;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.asLiveData;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoModel;

public class ViewModel_Photo extends AndroidViewModel {
    private Realm realm;
    private PhotoRepository photoRepository;
    private LiveData<AccessToken> accessTokenLiveData;
    private LiveData<RealmResults<Photo>> photoListLiveData;
    private LiveData<RealmResults<ViewInfo_Photo>> viewInfo_photoLiveData;

    public ViewModel_Photo(Application application) {
        super(application);
        realm = Realm.getDefaultInstance();
        photoRepository = new PhotoRepository();
        RealmResults<ViewInfo_Photo> results = realm.where(ViewInfo_Photo.class).findAll();
        if (results.size() == 0) {
            ViewInfo_Photo viewInfo_photo = new ViewInfo_Photo();
            realm.beginTransaction();
            realm.insert(viewInfo_photo);
            realm.commitTransaction();
        } else {
            realm.beginTransaction();
            results.get(0).place = null;
            realm.commitTransaction();
        }
    }

    @Override
    protected void onCleared() {
        realm.close();
        super.onCleared();
    }

    public void dataSyncPhotos() {
        photoRepository.getPhotos(realm);
    }

    public void dataSyncPlacePhotos(final String place) {
        photoRepository.getPlacePhotos(realm, place);
    }

    public void deletePhotos() {
        photoRepository.deletePhotos(realm);
    }

    public LiveData<RealmResults<Photo>> getPhotos() {
        photoListLiveData = photoModel(realm).findPhotos();
        return photoListLiveData;
    }

    public LiveData<AccessToken> getAccessToken() {
        LiveRealmData<AccessToken> accessTokenLiveRealmData = accessTokenModel(realm).findAccessToken();
        accessTokenLiveData = Transformations.map(accessTokenLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return accessTokenLiveData;
    }

    public LiveData<RealmResults<ViewInfo_Photo>> getViewInfo_Photo() {
        viewInfo_photoLiveData = asLiveData(realm.where(ViewInfo_Photo.class).findAllAsync());
        return viewInfo_photoLiveData;
    }

    public void setPlace(final String place) {
        ViewInfo_Photo viewInfo_photo = realm.where(ViewInfo_Photo.class).findFirst();
        realm.beginTransaction();
        viewInfo_photo.place = place;
        realm.commitTransaction();
    }
}
