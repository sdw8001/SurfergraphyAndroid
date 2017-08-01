package com.surfergraphy.surfergraphy.base.interfaces;

import retrofit2.Response;

public interface ResponseAction_Default<T> extends ResponseAction_Ok<T>, ResponseAction_OkCreated<T>, ResponseAction_BadRequest<T>, ResponseAction_UnAuthorized<T>, ResponseAction_NotFound<T> {
    void error(Response<T> response);
}
