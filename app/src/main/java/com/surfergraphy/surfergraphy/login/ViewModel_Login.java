package com.surfergraphy.surfergraphy.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surfergraphy.base.viewmodel.BaseViewModel;
import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.login.data.repositories.LoginRepository;
import com.surfergraphy.surfergraphy.base.interfaces.ResponseAction_Default;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.authorizationAccountUserDao;

public class ViewModel_Login extends BaseViewModel {

    private LoginRepository loginRepository;
    private LiveData<AccessToken> accessTokenLiveData;

    public ViewModel_Login(Application application) {
        super(application);
        loginRepository = new LoginRepository(realm);
    }

    public void loginAccount(final String identity, final String password) {
        loginRepository.loginAccount(identity, password);
    }

    public LiveData<AccessToken> getAccessToken() {
        LiveRealmData<AccessToken> accessTokenLiveRealmData = accessTokenModel(realm).findAccessToken();
        accessTokenLiveData = Transformations.map(accessTokenLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return accessTokenLiveData;
    }

    public void logoutAccount() {
        accessTokenModel(realm).deleteAccessToken();
        authorizationAccountUserDao(realm).deleteAuthorizationAccountUser();
    }

}
