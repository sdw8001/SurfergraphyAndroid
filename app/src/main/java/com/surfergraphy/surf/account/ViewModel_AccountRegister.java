package com.surfergraphy.surf.account;

import android.app.Application;

import com.surfergraphy.surf.account.data.RequestModel_AccountRegister;
import com.surfergraphy.surf.account.data.repository.AccountRepository;
import com.surfergraphy.surf.base.viewmodel.BaseViewModel;

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
