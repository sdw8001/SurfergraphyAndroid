package com.surfergraphy.surf.wavepurchase.data.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surf.base.data.ActionResponse;
import com.surfergraphy.surf.base.data.repositories.BaseRepository;
import com.surfergraphy.surf.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surf.utils.DateDeserializer;
import com.surfergraphy.surf.utils.ResponseAction;
import com.surfergraphy.surf.utils.RetrofitAdapter;
import com.surfergraphy.surf.wavepurchase.data.PurchaseInfo;
import com.surfergraphy.surf.wavepurchase.data.WavePurchase;
import com.surfergraphy.surf.wavepurchase.data.api.PurchaseService;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.surfergraphy.surf.utils.RealmUtils.wavePurchaseModel;

public class WavePurchasePhotoRepository extends BaseRepository {
    public interface ResponseCallback {
        void onSuccess();
        void onFail(String message);
    }

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
        wavePurchases.add(new WavePurchase("wave5", 5, 1100));

        // 15개 : 2200원
        wavePurchases.add(new WavePurchase("wave15", 15, 2200));

        // 55개 : 5500원
        wavePurchases.add(new WavePurchase("wave55", 55, 5500));

        // 120개 : 11000원
        wavePurchases.add(new WavePurchase("wave120", 120, 11000));

        // 280개 : 22000원
        wavePurchases.add(new WavePurchase("wave280", 280, 22000));

        // 700개 : 55000원
        wavePurchases.add(new WavePurchase("wave700", 700, 55000));

        // 1650개 : 110000원
        wavePurchases.add(new WavePurchase("wave1650", 1650, 110000));

        // 4000개 : 220000원
        wavePurchases.add(new WavePurchase("wave4000", 4000, 220000));

        createOrUpdateWavePurchases(wavePurchases);
    }

    public void addPurchase(final ResponseCallback responseCallback, final String userId, final String orderId, final String itemType, final String packageName, final String sku, final long purchaseTime, final int purchaseState, final String developerPayload, final String purchaseToken, final String originalJson, final String signature) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        PurchaseService purchaseService = retrofit.create(PurchaseService.class);

        final Call<PurchaseInfo> call = purchaseService.addPurchase(userId, orderId, itemType, packageName, sku, purchaseTime, purchaseState, developerPayload, purchaseToken, originalJson, signature);
        call.enqueue(new Callback<PurchaseInfo>() {
            @Override
            public void onResponse(Call<PurchaseInfo> call, Response<PurchaseInfo> response) {
                new ResponseAction<>(response, new ResponseAction_Default<PurchaseInfo>() {
                    @Override
                    public void error(Response<PurchaseInfo> response) {
                        if (responseCallback != null)
                            responseCallback.onFail(response.message());
                    }

                    @Override
                    public void badRequest(Response<PurchaseInfo> response, ActionResponse actionResponse) {

                    }

                    @Override
                    public void notFound(Response<PurchaseInfo> response) {

                    }

                    @Override
                    public void ok(Response<PurchaseInfo> response) {

                    }

                    @Override
                    public void okCreated(Response<PurchaseInfo> response) {

                        syncAuthorizationAccountUser();
                        if (responseCallback != null)
                            responseCallback.onSuccess();
                    }

                    @Override
                    public void unAuthorized(Response<PurchaseInfo> response) {
                        updateExpiredAccessToken(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<PurchaseInfo> call, Throwable t) {
                new Throwable("getPhoto Failed", t);
            }
        });
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
