package com.surfergraphy.surfergraphy.base.interfaces;

import retrofit2.Response;

public interface ResponseAction_OkCreated<T> {
    void okCreated(Response<T> response);  //  201
}
