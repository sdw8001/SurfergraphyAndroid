package com.surfergraphy.surf.wavepurchase.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class PurchaseInfo implements RealmModel{
    @SerializedName("OrderId")
    @PrimaryKey
    public String orderId;
    @SerializedName("ItemType")
    public String itemType;
    @SerializedName("PackageName")
    public String packageName;
    @SerializedName("Sku")
    public String sku;
    @SerializedName("PurchaseTime")
    public long purchaseTime;
    @SerializedName("PurchaseState")
    public int purchaseState;
    @SerializedName("DeveloperPayload")
    public String developerPayload;
    @SerializedName("PurchaseToken")
    public String purchaseToken;
    @SerializedName("OriginalJson")
    public String originalJson;
    @SerializedName("Signature")
    public String signature;

    public PurchaseInfo() {}

    public PurchaseInfo(String orderId, String itemType, String packageName, String sku, long purchaseTime, int purchaseState, String developerPayload, String purchaseToken, String originalJson, String signature) {
        this.orderId = orderId;
        this.itemType = itemType;
        this.packageName = packageName;
        this.sku = sku;
        this.purchaseTime = purchaseTime;
        this.purchaseState = purchaseState;
        this.developerPayload = developerPayload;
        this.purchaseToken = purchaseToken;
        this.originalJson = originalJson;
        this.signature = signature;
    }
}
