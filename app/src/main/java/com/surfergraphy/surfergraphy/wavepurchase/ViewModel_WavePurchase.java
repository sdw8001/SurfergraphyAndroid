package com.surfergraphy.surfergraphy.wavepurchase;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import com.surfergraphy.surfergraphy.base.helper.InAppBillingHelper;
import com.surfergraphy.surfergraphy.base.util.IabResult;
import com.surfergraphy.surfergraphy.base.util.Inventory;
import com.surfergraphy.surfergraphy.base.util.Purchase;
import com.surfergraphy.surfergraphy.base.viewmodel.BaseViewModel;
import com.surfergraphy.surfergraphy.wavepurchase.data.WavePurchase;
import com.surfergraphy.surfergraphy.wavepurchase.data.repositories.WavePurchasePhotoRepository;
import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import java.util.ArrayList;

import io.realm.RealmResults;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.wavePurchaseModel;

public class ViewModel_WavePurchase extends BaseViewModel {

    private WavePurchasePhotoRepository wavePurchasePhotoRepository;
    private LiveData<AccessToken> accessTokenLiveData;
    private LiveData<RealmResults<WavePurchase>> wavePurchasesLiveData;
    private InAppBillingHelper inAppBillingHelper;

    public ViewModel_WavePurchase(Application application) {
        super(application);
        wavePurchasePhotoRepository = WavePurchasePhotoRepository.getInstance(realm);
    }

    public void syncDataUserPhotos() {
        wavePurchasePhotoRepository.syncDataWavePurchase();
    }

    public LiveData<RealmResults<WavePurchase>> getWavePurchasesLiveData() {
        wavePurchasesLiveData = wavePurchaseModel(realm).findWavePurchaseLiveData();
        return wavePurchasesLiveData;
    }

    public LiveData<AccessToken> getAccessToken() {
        LiveRealmData<AccessToken> accessTokenLiveRealmData = accessTokenModel(realm).findAccessToken();
        accessTokenLiveData = Transformations.map(accessTokenLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return accessTokenLiveData;
    }

    public RealmResults<WavePurchase> getWavePurchases() {
        return wavePurchaseModel(realm).findWavePurchases();
    }

    public InAppBillingHelper getInAppBillingHelper() {
        return inAppBillingHelper;
    }

    public void setInAppBillingHelper(InAppBillingHelper inAppBillingHelper) {
        ArrayList<String> items = new ArrayList<>();
        for (WavePurchase item : getWavePurchases()) {
            items.add(item.id);
        }
        inAppBillingHelper.startSetup(items, new InAppBillingHelper.InventoryLoadListener() {
            @Override
            public void onBefore() {
                Log.e(ViewModel_WavePurchase.class.getSimpleName(), "InAppBilling.startSetup - Before");
            }

            @Override
            public void onSuccess(Inventory inventory) {
                Log.e(ViewModel_WavePurchase.class.getSimpleName(), "InAppBilling.startSetup - Success");
            }

            @Override
            public void onFail(IabResult result) {
                Log.e(ViewModel_WavePurchase.class.getSimpleName(), "InAppBilling.startSetup - Fail");
            }
        });
        this.inAppBillingHelper = inAppBillingHelper;
    }

    public void buyWavePurchase(WavePurchase wavePurchase) {
        inAppBillingHelper.purchaseItem(wavePurchase.id, new InAppBillingHelper.PurchaseFinishedListener() {
            @Override
            public void onSuccess(Purchase purchase) {
                Log.e(ViewModel_WavePurchase.class.getSimpleName(), "InAppBilling.purchaseItem - Success");
                inAppBillingHelper.consumeItem(wavePurchase.id, purchase);
            }

            @Override
            public void onFail(IabResult result) {
                Log.e(ViewModel_WavePurchase.class.getSimpleName(), "InAppBilling.purchaseItem - Fail");
            }
        });
    }
}
