package com.surfergraphy.surf.base.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelState {
    @SerializedName("detailMessage")
    private List<String> detailMessages;

    public List<String> getDetailMessages() {
        return detailMessages;
    }

    public void setDetailMessages(List<String> detailMessages) {
        this.detailMessages = detailMessages;
    }
}
