package com.surfergraphy.surfergraphy.intro;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.surfergraphy.surfergraphy.login.data.repositories.LoginRepository;

import io.realm.Realm;

public class ViewModel_Intro extends AndroidViewModel {

    private Realm realm;
    private LoginRepository loginRepository;

    public ViewModel_Intro(Application application) {
        super(application);
        realm = Realm.getDefaultInstance();
        loginRepository = new LoginRepository();
    }

    @Override
    protected void onCleared() {
        realm.close();
        super.onCleared();
    }

    public void getAuthorizationAccountUser() {
        loginRepository.getAuthorizationAccountUser(realm);
    }
}
