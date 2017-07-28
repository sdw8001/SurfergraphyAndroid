package com.surfergraphy.surfergraphy.base.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.login.data.AuthorizationAccountUser;
import com.surfergraphy.surfergraphy.login.data.repositories.LoginRepository;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.authorizationAccountUserDao;

public class BaseViewModel extends AndroidViewModel {

    protected Realm realm;

    public BaseViewModel(Application application) {
        super(application);
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onCleared() {
        realm.close();
        super.onCleared();
    }

    public AuthorizationAccountUser getAccountUser() {
        return authorizationAccountUserDao(realm).findAuthorizationAccountUser();
    }
}
