package com.surfergraphy.surf.base.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.surfergraphy.surf.base.data.ActionResponse;
import com.surfergraphy.surf.base.data.repositories.BaseRepository;
import com.surfergraphy.surf.login.data.LoginMember;
import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surf.utils.RealmUtils.actionResponseModel;
import static com.surfergraphy.surf.utils.RealmUtils.loginMemberModel;

public class BaseViewModel extends AndroidViewModel {

    protected Realm realm;
    protected BaseRepository baseRepository;
    protected LiveData<ActionResponse> actionResponseLiveData;
    protected LiveData<LoginMember> loginMemberLiveData;

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

    public LiveData<LoginMember> getLoginMemberLiveData() {
        LiveRealmData<LoginMember> loginMemberLiveRealmData = loginMemberModel(realm).findLoginMemberLiveRealmData();
        loginMemberLiveData = Transformations.map(loginMemberLiveRealmData, input -> input.size() > 0 ? input.get(0) : null);
        return loginMemberLiveData;
    }

    public LoginMember getLoginMember() {
        return loginMemberModel(realm).findLoginMember();
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
        baseRepository.syncLoginMember();
    }
}
