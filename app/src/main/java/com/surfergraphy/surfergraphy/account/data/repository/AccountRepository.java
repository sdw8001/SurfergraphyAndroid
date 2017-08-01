package com.surfergraphy.surfergraphy.account.data.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.surfergraphy.surfergraphy.account.data.RequestModel_AccountRegister;
import com.surfergraphy.surfergraphy.account.data.api.AccountService;
import com.surfergraphy.surfergraphy.base.ActionCode;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.data.ApiError;
import com.surfergraphy.surfergraphy.base.data.repositories.BaseRepository;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surfergraphy.utils.DateDeserializer;
import com.surfergraphy.surfergraphy.utils.ErrorUtils;
import com.surfergraphy.surfergraphy.utils.ResponseAction;
import com.surfergraphy.surfergraphy.utils.RetrofitAdapter;

import org.threeten.bp.LocalDateTime;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.actionResponseModel;

public class AccountRepository extends BaseRepository {

    public AccountRepository(Realm realm) {
        super(realm);
    }

    public void getAccountRegister(final int actionCode, final RequestModel_AccountRegister requestModel) {
        Gson gson = new GsonBuilder().setDateFormat("EEE',' dd MMM yyyy HH:mm:ss 'GMT'").registerTypeAdapter(LocalDateTime.class, new DateDeserializer()).create();
        Retrofit retrofit = RetrofitAdapter.getInstance(RetrofitAdapter.API_SERVER_URL, gson);
        AccountService accountService = retrofit.create(AccountService.class);

        final Call<ResponseBody> call = accountService.accountRegister(requestModel.Email, requestModel.Password, requestModel.ConfirmPassword, requestModel.NickName, requestModel.PhoneNumber);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                new ResponseAction<>(response, new ResponseAction_Default<ResponseBody>() {
                    @Override
                    public void error(Response<ResponseBody> response) {

                    }

                    @Override
                    public void badRequest(Response<ResponseBody> response, ActionResponse actionResponse) {
                        ApiError error = ErrorUtils.parseError(response);
                        if (error.getModelState().getDetailMessages().size() > 0)
                            actionResponse.setDetailMessages(error.getModelState().getDetailMessages().get(0));
                        actionResponse.setActionCode(actionCode);
                        createOrUpdateActionResponse(actionResponse);
                    }

                    @Override
                    public void notFound(Response<ResponseBody> response) {

                    }

                    @Override
                    public void ok(Response<ResponseBody> response) {
                        ActionResponse actionResponse = new ActionResponse();
                        actionResponse.setActionCode(actionCode);
                        actionResponse.setResultCode(response.code());
                        actionResponse.setMessage(response.message());
                        createOrUpdateActionResponse(actionResponse);
                    }

                    @Override
                    public void okCreated(Response<ResponseBody> response) {

                    }

                    @Override
                    public void unAuthorized(Response<ResponseBody> response) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new Throwable("getAuthorizationAccountUser Failed", t);
            }
        });
    }
}
