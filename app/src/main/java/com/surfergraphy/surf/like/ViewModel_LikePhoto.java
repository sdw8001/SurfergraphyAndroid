package com.surfergraphy.surf.like;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surf.base.viewmodel.BaseViewModel;
import com.surfergraphy.surf.like.data.LikePhoto;
import com.surfergraphy.surf.like.data.repositories.LikePhotoRepository;
import com.surfergraphy.surf.photos.data.repositories.PhotoBuyHistoryRepository;
import com.surfergraphy.surf.photos.data.repositories.PhotoRepository;
import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.RealmResults;

import static com.surfergraphy.surf.utils.RealmUtils.likePhotoModel;

public class ViewModel_LikePhoto extends BaseViewModel {

    private PhotoRepository photoRepository;
    private LikePhotoRepository likePhotoRepository;
    private PhotoBuyHistoryRepository photoBuyHistoryRepository;
    private LiveData<LikePhoto> likePhotoLiveData;
    private LiveData<RealmResults<LikePhoto>> likePhotoListLiveData;

    public ViewModel_LikePhoto(Application application) {
        super(application);
        photoRepository = PhotoRepository.getInstance(realm);
        likePhotoRepository = LikePhotoRepository.getInstance(realm);
        photoBuyHistoryRepository = PhotoBuyHistoryRepository.getInstance(realm);
    }

    public void syncDataLikePhotos() {
        photoRepository.syncPhotoFromLikePhotos();
        photoBuyHistoryRepository.syncDataPhotoByHistories();
        likePhotoRepository.syncDataUserPhotos();
    }

    public LiveData<RealmResults<LikePhoto>> getLikePhotos() {
        likePhotoListLiveData = likePhotoModel(realm).findLikePhotosLiveData(getLoginMember().Id);
        return likePhotoListLiveData;
    }

    public void likePhoto(final int actionCode, final String userId, final int photoId) {
        likePhotoRepository.likePhoto(actionCode, userId, photoId);
    }

    public void cancelLikePhoto(final int actionCode, final int id) {
        likePhotoRepository.cancelLikePhoto(actionCode, id);
    }

    public LiveData<LikePhoto> getLikePhoto(final String userId, final int photoId) {
        LiveRealmData<LikePhoto> liveRealmData = likePhotoModel(realm).findLikePhotoLiveData(userId, photoId);
        likePhotoLiveData = Transformations.map(liveRealmData, data -> data.size() > 0 ? data.get(0) : null);
        return likePhotoLiveData;
    }

    public String getLikePhotoUrl(final int id) {
        LikePhoto likePhoto = realm.where(LikePhoto.class).equalTo("id", id).findFirst();
        if (likePhoto != null) {
            return likePhoto.photo.url;
        }
        return null;
    }

    public void toggleSelectableLikePhoto(final String userId) {
        likePhotoRepository.toggleSelectableLikePhoto(userId);
    }
}
