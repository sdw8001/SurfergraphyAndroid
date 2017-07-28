package com.surfergraphy.surfergraphy.photos.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Photo implements RealmModel{


    @SerializedName("Id")
    @PrimaryKey
    public int id;
    @SerializedName("Name")
    public String name;
    @SerializedName("Url")
    public String url;
    @SerializedName("Place")
    public String place;
    @SerializedName("MimeType")
    public String mimeType;
    @SerializedName("Wave")
    public int wave;

    public Photo() {}

    public Photo(int id, String name, String url, String place, String mimeType, int wave) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.place = place;
        this.mimeType = mimeType;
        this.wave = wave;
    }
}
