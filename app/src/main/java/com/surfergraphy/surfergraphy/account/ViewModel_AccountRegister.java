package com.surfergraphy.surfergraphy.account;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surfergraphy.account.data.RequestModel_AccountRegister;
import com.surfergraphy.surfergraphy.account.data.repository.AccountRepository;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.viewmodel.BaseViewModel;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.actionResponseModel;

public class ViewModel_AccountRegister extends BaseViewModel {

    private AccountRepository accountRepository;
    private LiveData<ActionResponse> actionResponseLiveData;

    public ViewModel_AccountRegister(Application application) {
        super(application);
        accountRepository = new AccountRepository();
    }

    public void requestAccountRegister(final int actionCode, final String email, final String password, final String passwordConfirm, final String nickName, final String phoneNumber) {
        RequestModel_AccountRegister requestModel = new RequestModel_AccountRegister(email, password, password, phoneNumber, nickName);
        accountRepository.getAccountRegister(realm, actionCode, requestModel);
    }

    public LiveData<ActionResponse> getActionResponse(final int actionCode) {
        LiveRealmData<ActionResponse> actionResponseLiveRealmData = actionResponseModel(realm).findActionResponse(actionCode);
        actionResponseLiveData = Transformations.map(actionResponseLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return actionResponseLiveData;
    }

    public void expiredActionToken(final int actionCode) {
        actionResponseModel(realm).deleteActionResponse(actionCode);
    }

}
