package com.surfergraphy.surf.photos;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surf.base.BaseType;
import com.surfergraphy.surf.base.viewmodel.BaseViewModel;
import com.surfergraphy.surf.login.data.LoginMember;
import com.surfergraphy.surf.photos.data.Photo;
import com.surfergraphy.surf.photos.data.PhotoDate;
import com.surfergraphy.surf.photos.data.ViewInfo_Photo;
import com.surfergraphy.surf.photos.data.repositories.PhotoRepository;
import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.RealmResults;

import static com.surfergraphy.surf.utils.RealmUtils.loginMemberModel;
import static com.surfergraphy.surf.utils.RealmUtils.asLiveData;
import static com.surfergraphy.surf.utils.RealmUtils.photoDateModel;
import static com.surfergraphy.surf.utils.RealmUtils.photoModel;

public class ViewModel_Photo extends BaseViewModel {

    private PhotoRepository photoRepository;
    private LiveData<RealmResults<PhotoDate>> photoDateListLiveData;
    private LiveData<RealmResults<Photo>> photoListLiveData;
    private LiveData<RealmResults<ViewInfo_Photo>> viewInfo_photoLiveData;

    public ViewModel_Photo(Application application) {
        super(application);
        photoRepository = new PhotoRepository(realm);
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

    public void dataSyncPhotos(final String date) {
        photoRepository.getPhotos(date);
    }

    public void dataSyncPlacePhotos(final BaseType.LocationType place, final String date) {
        photoRepository.getPlacePhotos(place.getCode(), date);
    }

    public void dataSyncDates() {
        photoRepository.getDate();
    }

    public void dataSyncDatesFromPlace(final BaseType.LocationType place) {
        photoRepository.getDateFromPlace(place.getCode());
    }

    public void deletePhotos() {
        photoRepository.deletePhotos();
    }

    public void deleteDatePhotos() {
        photoRepository.deleteDatePhotos();
    }

    public LiveData<RealmResults<Photo>> getPhotos() {
        photoListLiveData = photoModel(realm).findPhotosLiveData();
        return photoListLiveData;
    }

    public LiveData<RealmResults<PhotoDate>> getDates() {
        photoDateListLiveData = photoDateModel(realm).findPhotoDateLiveData();
        return photoDateListLiveData;
    }

    public LiveData<RealmResults<ViewInfo_Photo>> getViewInfo_Photo() {
        viewInfo_photoLiveData = asLiveData(realm.where(ViewInfo_Photo.class).findAllAsync());
        return viewInfo_photoLiveData;
    }

    public void setPlace(final BaseType.LocationType place) {
        ViewInfo_Photo viewInfo_photo = realm.where(ViewInfo_Photo.class).findFirst();
        realm.beginTransaction();
        viewInfo_photo.place = place.getCode();
        realm.commitTransaction();
    }
}
