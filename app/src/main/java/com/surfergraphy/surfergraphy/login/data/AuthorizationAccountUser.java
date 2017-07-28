package com.surfergraphy.surfergraphy.login.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class AuthorizationAccountUser implements RealmModel {
    @SerializedName("Id")
    @PrimaryKey
    public String id;
    @SerializedName("Email")
    public String email;
    @SerializedName("PhoneNumber")
    public String phoneNumber;
    @SerializedName("NickName")
    public String nickName;
    @SerializedName("Wave")
    public int wave;
    @SerializedName("HasRegistered")
    public boolean hasRegistered;

    public AuthorizationAccountUser() {
    }

    public AuthorizationAccountUser(String id, String email, String phoneNumber, String nickName, int wave, boolean hasRegistered) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
        this.wave = wave;
        this.hasRegistered = hasRegistered;
    }
}
