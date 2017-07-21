package com.surfergraphy.surfergraphy.base.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class ActionResponse implements RealmModel {
    @PrimaryKey
    private int id;
    private int actionCode;
    private int resultCode;
    @SerializedName("Message")
    private String message;
    @SerializedName("ModelState")
    private String detailMessages;
    private boolean expired;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetailMessages() {
        return detailMessages;
    }

    public void setDetailMessages(String detailMessages) {
        this.detailMessages = detailMessages;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
