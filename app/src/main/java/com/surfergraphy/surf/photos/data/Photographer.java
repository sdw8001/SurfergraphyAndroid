package com.surfergraphy.surf.photos.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Photographer implements RealmModel{

    @SerializedName("Id")
    @PrimaryKey
    public String id;
    @SerializedName("Password")
    public String password;
    @SerializedName("Name")
    public String name;
    @SerializedName("NickName")
    public String nickName;
    @SerializedName("Tel")
    public String tel;
    @SerializedName("Image")
    public String image;
    @SerializedName("Grade")
    public String grade;
    @SerializedName("Deleted")
    public boolean deleted;

    public Photographer() {}
}
