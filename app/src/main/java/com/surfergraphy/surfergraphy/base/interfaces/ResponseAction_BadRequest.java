package com.surfergraphy.surfergraphy.base.interfaces;

import retrofit2.Response;

public interface ResponseAction_BadRequest<T> {
    void badRequest(Response<T> response); //  400
}
