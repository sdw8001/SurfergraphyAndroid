package com.surfergraphy.surfergraphy.utils;

import com.surfergraphy.surfergraphy.base.data.ApiError;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {
    public static ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, null).responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (Exception e) {
            return new ApiError();
        }

        return error;
    }
}
