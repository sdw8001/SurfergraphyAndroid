package com.surfergraphy.surf.wavepurchase.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class WavePurchase implements RealmModel{


    @SerializedName("Id")
    @PrimaryKey
    public String id;
    @SerializedName("WaveCount")
    public int waveCount;
    @SerializedName("WavePrice")
    public int wavePrice;

    public WavePurchase() {}

    public WavePurchase(String id, int waveCount, int wavePrice) {
        this.id = id;
        this.waveCount = waveCount;
        this.wavePrice = wavePrice;
    }
}
