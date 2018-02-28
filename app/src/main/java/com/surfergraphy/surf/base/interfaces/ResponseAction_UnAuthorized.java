package com.surfergraphy.surf.base.interfaces;

import retrofit2.Response;

/**
 * Created by ddfactory on 2017-07-06.
 */

public interface ResponseAction_UnAuthorized<T> {
    void unAuthorized(Response<T> response);  //  401
}
