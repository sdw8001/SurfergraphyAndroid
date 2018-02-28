package com.surfergraphy.surf.wavepurchase.data.api;

import com.surfergraphy.surf.wavepurchase.data.PurchaseInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PurchaseService {

    @GET("api/Purchases")
    Call<List<PurchaseInfo>> getPurchases();

    @FormUrlEncoded
    @POST("api/Purchases/")
    Call<PurchaseInfo> addPurchase(@Field("UserId") String userId, @Field("OrderId") String orderId, @Field("ItemType") String itemType, @Field("PackageName") String packageName, @Field("Sku") String sku, @Field("PurchaseTime") long purchaseTime, @Field("purchaseState") int purchaseState, @Field("DeveloperPayload") String developerPayload,
                                   @Field("PurchaseToken") String purchaseToken, @Field("OriginalJson") String originalJson, @Field("Signature") String signature);
}
