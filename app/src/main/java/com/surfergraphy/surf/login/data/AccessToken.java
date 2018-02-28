package com.surfergraphy.surf.login.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class AccessToken implements RealmModel {

    @SerializedName("userName")
    @PrimaryKey
    public String userName;
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("expires_in")
    public int expireTimeSeconds;
    public boolean expired;

    public AccessToken() {
        this.expired = false;
    }

    public AccessToken(String userName, String accessToken, String tokenType, int expireTimeSeconds) {
        this.userName = userName;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expireTimeSeconds = expireTimeSeconds;
        this.expired = false;
    }
}