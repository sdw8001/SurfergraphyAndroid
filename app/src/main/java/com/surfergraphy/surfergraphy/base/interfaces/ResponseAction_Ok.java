package com.surfergraphy.surfergraphy.base.interfaces;

import retrofit2.Response;

public interface ResponseAction_Ok<T> {
    void ok(Response<T> response);  //  200
}
