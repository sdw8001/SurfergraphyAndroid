package com.surfergraphy.surf.photos.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class PhotoBuyHistory implements RealmModel {

    @SerializedName("Id")
    @PrimaryKey
    public int id;
    @SerializedName("UserId")
    public String userId;
    @SerializedName("PhotoId")
    public int photoId;
    @SerializedName("Wave")
    public int wave;
    @SerializedName("Paid")
    public boolean paid;
    @SerializedName("PaidDate")
    public String paidDate;

    public PhotoBuyHistory() {}

    public PhotoBuyHistory(int id, String userId, int photoId, int wave, boolean paid, String paidDate) {
        this.id = id;
        this.userId = userId;
        this.photoId = photoId;
        this.wave = wave;
        this.paid = paid;
        this.paidDate = paidDate;
    }
}
