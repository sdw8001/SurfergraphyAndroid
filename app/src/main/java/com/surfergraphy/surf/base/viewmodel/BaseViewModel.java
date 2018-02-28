package com.surfergraphy.surf.base.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surf.base.data.ActionResponse;
import com.surfergraphy.surf.base.data.repositories.BaseRepository;
import com.surfergraphy.surf.login.data.AuthorizationAccountUser;
import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surf.utils.RealmUtils.actionResponseModel;
import static com.surfergraphy.surf.utils.RealmUtils.authorizationAccountUserDao;

public class BaseViewModel extends AndroidViewModel {

    protected Realm realm;
    protected BaseRepository baseRepository;
    protected LiveData<ActionResponse> actionResponseLiveData;
    protected LiveData<AuthorizationAccountUser> authorizationAccountUserLiveData;

    public BaseViewModel(Application application) {
        super(application);
        realm = Realm.getDefaultInstance();
        this.baseRepository = new BaseRepository(realm);
    }

    @Override
    protected void onCleared() {
        realm.close();
        super.onCleared();
    }

    public LiveData<AuthorizationAccountUser> getAccountUserLiveData() {
        LiveRealmData<AuthorizationAccountUser> accessTokenLiveRealmData = authorizationAccountUserDao(realm).findAuthorizationAccountUserLiveData();
        authorizationAccountUserLiveData = Transformations.map(accessTokenLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return authorizationAccountUserLiveData;
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

    public void syncAuthorizationAccountUser() {
        baseRepository.syncAuthorizationAccountUser();
    }
}
