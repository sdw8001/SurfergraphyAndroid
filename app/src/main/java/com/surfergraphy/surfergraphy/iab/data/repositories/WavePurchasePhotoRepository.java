package com.surfergraphy.surfergraphy.iab.data.repositories;

import com.surfergraphy.surfergraphy.base.data.repositories.BaseRepository;
import com.surfergraphy.surfergraphy.iab.data.WavePurchase;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.wavePurchaseModel;

public class WavePurchasePhotoRepository extends BaseRepository {

    private static WavePurchasePhotoRepository instance;
    public static WavePurchasePhotoRepository getInstance(Realm realm) {
        if (instance == null)
            instance = new WavePurchasePhotoRepository(realm);
        return instance;
    }

    private WavePurchasePhotoRepository(Realm realm) {
        super(realm);
        syncDataWavePurchase();
    }

    public void syncDataWavePurchase() {
        deleteWavePurchases();
        ArrayList<WavePurchase> wavePurchases = new ArrayList<>();

        // 5개 : 1100원
        wavePurchases.add(new WavePurchase(1, 5, 1100));

        // 15개 : 2200원
        wavePurchases.add(new WavePurchase(2, 15, 2200));

        // 55개 : 5500원
        wavePurchases.add(new WavePurchase(3, 55, 5500));

        // 120개 : 12000원
        wavePurchases.add(new WavePurchase(4, 120, 12000));

        // 280개 : 22000원
        wavePurchases.add(new WavePurchase(5, 280, 22000));

        // 700개 : 55000원
        wavePurchases.add(new WavePurchase(6, 700, 55000));

        // 1650개 : 110000원
        wavePurchases.add(new WavePurchase(7, 1650, 110000));

        // 4000개 : 220000원
        wavePurchases.add(new WavePurchase(8, 4000, 220000));

        createOrUpdateWavePurchases(wavePurchases);
    }

    public void deleteWavePurchases() {
        realm.beginTransaction();
        wavePurchaseModel(realm).deleteWavePurchase();
        realm.commitTransaction();
    }

    private void createOrUpdateWavePurchases(final List<WavePurchase> wavePurchases) {
        if (wavePurchases == null)
            return;

        for (WavePurchase wavePurchase : wavePurchases) {
            realm.beginTransaction();
            wavePurchaseModel(realm).createOrUpdate(wavePurchase);
            realm.commitTransaction();
        }
    }
}
