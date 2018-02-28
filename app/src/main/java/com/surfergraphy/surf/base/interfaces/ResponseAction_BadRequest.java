package com.surfergraphy.surf.base.interfaces;

import com.surfergraphy.surf.base.data.ActionResponse;

import retrofit2.Response;

public interface ResponseAction_BadRequest<T> {
    void badRequest(Response<T> response, ActionResponse actionResponse); //  400
}
