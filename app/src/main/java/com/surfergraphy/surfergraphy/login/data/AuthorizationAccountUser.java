package com.surfergraphy.surfergraphy.login.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class AuthorizationAccountUser implements RealmModel {
    @SerializedName("Email")
    @PrimaryKey
    public String email;
    @SerializedName("HasRegistered")
    public boolean hasRegistered;

    public AuthorizationAccountUser() {
    }

    public AuthorizationAccountUser(String email, boolean hasRegistered) {
        this.email = email;
        this.hasRegistered = hasRegistered;
    }
}
