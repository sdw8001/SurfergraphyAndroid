package com.surfergraphy.surfergraphy.base.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surfergraphy.base.data.ActionResponse;
import com.surfergraphy.surfergraphy.login.data.AccessToken;
import com.surfergraphy.surfergraphy.login.data.AuthorizationAccountUser;
import com.surfergraphy.surfergraphy.login.data.repositories.LoginRepository;
import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.accessTokenModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.actionResponseModel;
import static com.surfergraphy.surfergraphy.utils.RealmUtils.authorizationAccountUserDao;

public class BaseViewModel extends AndroidViewModel {

    protected Realm realm;
    protected LiveData<ActionResponse> actionResponseLiveData;

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

    public LiveData<ActionResponse> getActionResponse(final int actionCode) {
        LiveRealmData<ActionResponse> actionResponseLiveRealmData = actionResponseModel(realm).findActionResponse(actionCode);
        actionResponseLiveData = Transformations.map(actionResponseLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return actionResponseLiveData;
    }

    public void expiredActionToken(final int actionCode) {
        actionResponseModel(realm).deleteActionResponse(actionCode);
    }
}
