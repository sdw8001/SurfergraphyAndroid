package com.surfergraphy.surf.intro;

import android.app.Application;

import com.surfergraphy.surf.base.viewmodel.BaseViewModel;
import com.surfergraphy.surf.login.data.repositories.LoginRepository;

public class ViewModel_Intro extends BaseViewModel {

    private LoginRepository loginRepository;

    public ViewModel_Intro(Application application) {
        super(application);
        loginRepository = new LoginRepository(realm);
    }

    public void syncLoginMember() {
        loginRepository.syncLoginMember();
    }
}
