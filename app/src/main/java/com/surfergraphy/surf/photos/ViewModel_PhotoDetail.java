package com.surfergraphy.surf.photos;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surf.album.data.repositories.UserPhotoRepository;
import com.surfergraphy.surf.base.viewmodel.BaseViewModel;
import com.surfergraphy.surf.login.data.LoginMember;
import com.surfergraphy.surf.photos.data.Photo;
import com.surfergraphy.surf.photos.data.PhotoBuyHistory;
import com.surfergraphy.surf.photos.data.PhotoSaveHistory;
import com.surfergraphy.surf.photos.data.Photographer;
import com.surfergraphy.surf.photos.data.repositories.PhotoBuyHistoryRepository;
import com.surfergraphy.surf.photos.data.repositories.PhotoSaveHistoryRepository;
import com.surfergraphy.surf.photos.data.repositories.PhotographerRepository;
import com.surfergraphy.surf.utils.LiveRealmData;

import static com.surfergraphy.surf.utils.RealmUtils.loginMemberModel;
import static com.surfergraphy.surf.utils.RealmUtils.photoBuyHistoryModel;
import static com.surfergraphy.surf.utils.RealmUtils.photoModel;
import static com.surfergraphy.surf.utils.RealmUtils.photoSaveHistoryModel;
import static com.surfergraphy.surf.utils.RealmUtils.photographerModel;

public class ViewModel_PhotoDetail extends BaseViewModel {

    private PhotoSaveHistoryRepository photoSaveHistoryRepository;
    private PhotoBuyHistoryRepository photoBuyHistoryRepository;
    private UserPhotoRepository userPhotoRepository;
    private PhotographerRepository photographerRepository;
    private LiveData<LoginMember> accessTokenLiveData;
    private LiveData<Photo> photoListLiveData;
    private LiveData<PhotoSaveHistory> photoSaveHistoryLiveData;
    private LiveData<PhotoBuyHistory> photoBuyHistoryLiveData;
    private LiveData<Photographer> photographerLiveData;

    public ViewModel_PhotoDetail(Application application) {
        super(application);
        photoSaveHistoryRepository = PhotoSaveHistoryRepository.getInstance(realm);
        photoBuyHistoryRepository = PhotoBuyHistoryRepository.getInstance(realm);
        userPhotoRepository = UserPhotoRepository.getInstance(realm);
        photographerRepository = PhotographerRepository.getInstance(realm);
    }

    public LiveData<Photo> getPhoto(int photoId) {
        LiveRealmData<Photo> photoLiveRealmData = photoModel(realm).findPhotoLiveData(photoId);
        photoListLiveData = Transformations.map(photoLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return photoListLiveData;
    }

    public LiveData<PhotoSaveHistory> getUserPhotoSaveHistory(final String userId, final int photoId) {
        LiveRealmData<PhotoSaveHistory> liveRealmData = photoSaveHistoryModel(realm).findPhotoSaveHistoryLiveData(userId, photoId);
        photoSaveHistoryLiveData = Transformations.map(liveRealmData, data -> data.size() > 0 ? data.get(0) : null);
        return photoSaveHistoryLiveData;
    }

    public LiveData<PhotoBuyHistory> getPhotoBuyHistory(final int photoId) {
        LiveRealmData<PhotoBuyHistory> liveRealmData = photoBuyHistoryModel(realm).findPhotoBuyHistoryLiveData(photoId);
        photoBuyHistoryLiveData = Transformations.map(liveRealmData, data -> data.size() > 0 ? data.get(0) : null);
        return photoBuyHistoryLiveData;
    }

    public void savePhoto(final int actionCode, final String userId, final int photoId) {
        photoSaveHistoryRepository.savePhoto(actionCode, userId, photoId, 0);
    }

    public void buyPhoto(final int actionCode, final String userId, final int photoId) {
        photoBuyHistoryRepository.buyPhoto(actionCode, userId, photoId);
    }

    public void dataSyncPhotographer(final String id) {
        photographerRepository.getPhotographer(id);
    }

    public LiveData<Photographer> getPhotographer(final String id) {
        LiveRealmData<Photographer> liveRealmData = photographerModel(realm).findPhotographerLiveData(id);
        photographerLiveData = Transformations.map(liveRealmData, data -> data.size() > 0 ? data.get(0) : null);
        return photographerLiveData;
    }
}
