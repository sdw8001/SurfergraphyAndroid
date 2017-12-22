package com.surfergraphy.surfergraphy.wavepurchase.data;

import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.asLiveData;

public class WavePurchaseDao {

    private Realm realm;

    public WavePurchaseDao(Realm realm) {
        this.realm = realm;
    }

    public WavePurchase createOrUpdate(WavePurchase wavePurchase) {
        if (wavePurchase != null)
            wavePurchase = realm.copyToRealmOrUpdate(wavePurchase);
        return wavePurchase;
    }

    public LiveRealmData<WavePurchase> findWavePurchaseLiveData() {
        return asLiveData(realm.where(WavePurchase.class).findAllAsync());
    }

    public RealmResults<WavePurchase> findWavePurchases() {
        return realm.where(WavePurchase.class).findAllAsync();
    }

    public void deleteWavePurchase() {
        realm.delete(WavePurchase.class);
    }
}
