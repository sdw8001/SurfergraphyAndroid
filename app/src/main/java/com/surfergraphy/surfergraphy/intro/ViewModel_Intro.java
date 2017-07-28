package com.surfergraphy.surfergraphy.intro;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.surfergraphy.surfergraphy.base.viewmodel.BaseViewModel;
import com.surfergraphy.surfergraphy.login.data.repositories.LoginRepository;

import io.realm.Realm;

public class ViewModel_Intro extends BaseViewModel {

    private LoginRepository loginRepository;

    public ViewModel_Intro(Application application) {
        super(application);
        loginRepository = new LoginRepository();
    }

    public void getAuthorizationAccountUser() {
        loginRepository.getAuthorizationAccountUser(realm);
    }
}
