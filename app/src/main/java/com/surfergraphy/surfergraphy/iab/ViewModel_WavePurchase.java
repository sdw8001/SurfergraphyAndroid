package com.surfergraphy.surfergraphy.iab;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.widget.Toast;

import com.surfergraphy.surfergraphy.base.viewmodel.BaseViewModel;
import com.surfergraphy.surfergraphy.iab.data.WavePurchase;
import com.surfergraphy.surfergraphy.iab.data.repositories.WavePurchasePhotoRepository;
import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.RealmResults;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.wavePurchaseModel;

public class ViewModel_WavePurchase extends BaseViewModel {

    private WavePurchasePhotoRepository wavePurchasePhotoRepository;
    private LiveData<AccessToken> accessTokenLiveData;
    private LiveData<RealmResults<WavePurchase>> wavePurchasesLiveData;

    public ViewModel_WavePurchase(Application application) {
        super(application);
        wavePurchasePhotoRepository = WavePurchasePhotoRepository.getInstance(realm);
    }

    public void syncDataUserPhotos() {
        wavePurchasePhotoRepository.syncDataWavePurchase();
    }

    public LiveData<RealmResults<WavePurchase>> getWavePurchases() {
        wavePurchasesLiveData = wavePurchaseModel(realm).findWavePurchaseLiveData();
        return wavePurchasesLiveData;
    }

    public LiveData<AccessToken> getAccessToken() {
        LiveRealmData<AccessToken> accessTokenLiveRealmData = accessTokenModel(realm).findAccessToken();
        accessTokenLiveData = Transformations.map(accessTokenLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return accessTokenLiveData;
    }

    public void buyWavePurchase(WavePurchase wavePurchase) {

    }
}
