package com.surfergraphy.surfergraphy.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.data.ApiError;
import com.surfergraphy.surfergraphy.base.data.ModelState;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by ddfactory on 2017-07-06.
 *
 */

public class ResponseAction<T> {

    public final static int HTTP_200_OK = 200;
    public final static int HTTP_201_OK_CREATED = 201;
    public final static int HTTP_400_BAD_REQUEST = 400;
    public final static int HTTP_401_UN_AUTHORIZED = 401;
    public final static int HTTP_404_NOT_FOUND = 404;

    private Response<T> response;
    private ResponseAction_Default responseAction_default;
    private ActionResponse actionResponse;
    private ApiError apiError;

    public ResponseAction(Response<T> response, ResponseAction_Default responseAction_default){
        if (response != null) {
            this.response = response;
            this.responseAction_default = responseAction_default;
//            Gson gson = new Gson();
//            ResponseBody errorBody = response.errorBody();
//            if (errorBody != null) {
//                String errorString = null;
//                try {
//                    errorString = errorBody.string();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (!TextUtils.isEmpty(errorString)) {
//                    actionResponse = gson.fromJson(errorString, ActionResponse.class);
//                    actionResponse.setResultCode(response.code());
//                }
//            }

            if (actionResponse == null) {
                this.actionResponse = new ActionResponse();
                this.actionResponse.setMessage(response.message());
                this.actionResponse.setResultCode(response.code());
            }
            this.action();
        }
    }

    private void action() {
        ResponseAction_Default callbackAction = this.responseAction_default;
        Response response = this.response;
        switch (response.code()) {
            case HTTP_200_OK:
                callbackAction.ok(response);
                break;
            case HTTP_201_OK_CREATED:
                callbackAction.okCreated(response);
                break;
            case HTTP_400_BAD_REQUEST:

                apiError = ErrorUtils.parseError(response);
                if (apiError.getErrorCode() == 0) {
                    String detailMessageSum = "";
                    for (String detailMessage : apiError.getModelState().getDetailMessages())
                        detailMessageSum = detailMessageSum + detailMessage + "\r\n";
                    if (!TextUtils.isEmpty(detailMessageSum))
                        actionResponse.setDetailMessages(detailMessageSum);
                } else {
                    actionResponse.setErrorCode(apiError.getErrorCode());
                    actionResponse.setMessage(apiError.getMessage());
                }
                callbackAction.badRequest(response, actionResponse);
                break;
            case HTTP_401_UN_AUTHORIZED:
                callbackAction.unAuthorized(response);
                break;
            case HTTP_404_NOT_FOUND:
                callbackAction.notFound(response);
                break;
            default:
                callbackAction.error(response);
        }
    }

}