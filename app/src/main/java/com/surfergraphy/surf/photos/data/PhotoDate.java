package com.surfergraphy.surf.photos.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class PhotoDate implements RealmModel{

    @PrimaryKey
    @SerializedName("DateString")
    public String date;

    public PhotoDate() {}
}
