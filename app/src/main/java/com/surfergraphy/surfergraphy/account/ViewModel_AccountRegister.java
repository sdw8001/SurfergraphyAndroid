package com.surfergraphy.surfergraphy.account;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surfergraphy.account.data.RequestModel_AccountRegister;
import com.surfergraphy.surfergraphy.account.data.repository.AccountRepository;
import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.base.data.repositories.BaseRepository;
import com.surfergraphy.surfergraphy.base.viewmodel.BaseViewModel;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.actionResponseModel;

public class ViewModel_AccountRegister extends BaseViewModel {

    private AccountRepository accountRepository;

    public ViewModel_AccountRegister(Application application) {
        super(application);
        accountRepository = new AccountRepository(realm);
    }

    public void requestAccountRegister(final int actionCode, final String email, final String password, final String passwordConfirm, final String nickName, final String phoneNumber) {
        RequestModel_AccountRegister requestModel = new RequestModel_AccountRegister(email, password, passwordConfirm, phoneNumber, nickName);
        accountRepository.getAccountRegister(actionCode, requestModel);
    }

}
