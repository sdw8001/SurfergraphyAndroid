package com.surfergraphy.surf.base.data;

import com.google.gson.annotations.SerializedName;

public class ApiError {
    private int actionCode;
    private int resultCode;
    @SerializedName("ErrorCode")
    private int errorCode;
    @SerializedName("Message")
    private String message;
    @SerializedName("ModelState")
    private ModelState modelState;
    private boolean expired;

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

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ModelState getModelState() {
        return modelState;
    }

    public void setModelState(ModelState modelState) {
        this.modelState = modelState;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

}
