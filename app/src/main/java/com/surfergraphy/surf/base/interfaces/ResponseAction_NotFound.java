package com.surfergraphy.surf.base.interfaces;

import retrofit2.Response;

public interface ResponseAction_NotFound<T> {
    void notFound(Response<T> response);  //  404
}
