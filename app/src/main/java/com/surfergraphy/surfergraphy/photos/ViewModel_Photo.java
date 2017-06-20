package com.surfergraphy.surfergraphy.photos;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surfergraphy.photos.data.Photo;
import com.surfergraphy.surfergraphy.photos.data.repositories.PhotoRepository;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.photoModel;

public class ViewModel_Photo extends AndroidViewModel {
    private Realm realm;
    private PhotoRepository photoRepository;
    private LiveData<Photo> photoLiveData;
    private LiveData<RealmResults<Photo>> photoListLiveData;

    public ViewModel_Photo(Application application) {
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

    public LiveData<RealmResults<Photo>> getPhotos() {
        photoListLiveData = photoModel(realm).findPhotos();
        return photoListLiveData;
    }

    public LiveData<Photo> getPhoto() {
        LiveRealmData<Photo> photoLiveRealmData = photoModel(realm).findPhotos();
        photoLiveData = Transformations.map(photoLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return photoLiveData;
    }
}
