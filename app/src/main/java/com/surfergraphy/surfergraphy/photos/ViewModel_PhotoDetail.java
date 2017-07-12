package com.surfergraphy.surfergraphy.photos;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.photos.data.repositories.PhotoRepository;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoModel;

public class ViewModel_PhotoDetail extends AndroidViewModel {
    private Realm realm;
    private PhotoRepository photoRepository;
    private LiveData<AccessToken> accessTokenLiveData;
    private LiveData<Photo> photoListLiveData;

    public ViewModel_PhotoDetail(Application application) {
        super(application);
        realm = Realm.getDefaultInstance();
        photoRepository = new PhotoRepository();
    }

    @Override
    protected void onCleared() {
        realm.close();
        super.onCleared();
    }

    public void dataSyncPhotos() {
        photoRepository.getPhotos(realm);
    }

    public LiveData<Photo> getPhoto(int photoId) {
        LiveRealmData<Photo> photoLiveRealmData = photoModel(realm).findPhotos(photoId);
        photoListLiveData = Transformations.map(photoLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return photoListLiveData;
    }

    public LiveData<AccessToken> getAccessToken() {
        LiveRealmData<AccessToken> accessTokenLiveRealmData = accessTokenModel(realm).findAccessToken();
        accessTokenLiveData = Transformations.map(accessTokenLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return accessTokenLiveData;
    }
}
