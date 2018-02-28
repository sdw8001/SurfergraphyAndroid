package com.surfergraphy.surf.photos.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Photo implements RealmModel{


    @SerializedName("Id")
    @PrimaryKey
    public int id;
    @SerializedName("PhotographerId")
    public String photographerId;
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
    @SerializedName("DimensionWidth")
    public int dimensionWidth;
    @SerializedName("DimensionHeight")
    public int dimensionHeight;
    @SerializedName("Resolution")
    public int resolution;
    @SerializedName("Date")
    public String createdDate;
    @SerializedName("ExpirationDate")
    public String expirationDate;

    public Photo() {}
}
