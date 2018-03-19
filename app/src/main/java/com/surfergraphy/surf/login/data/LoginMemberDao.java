package com.surfergraphy.surf.login.data;

import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surf.utils.RealmUtils.asLiveData;

public class LoginMemberDao {

    private Realm realm;

    public LoginMemberDao(Realm realm) {
        this.realm = realm;
    }

    public LoginMember createOrUpdate(LoginMember loginMember) {
        if (loginMember != null) {
            loginMember = realm.copyToRealmOrUpdate(loginMember);
        }
        return loginMember;
    }

    public void removeLoginMember() {
        realm.executeTransactionAsync(realm1 -> realm1.delete(LoginMember.class));
    }

    public void updateExpiredLoginMember(boolean expired) {
        realm.executeTransactionAsync(realm1 -> {
            LoginMember loginMember = realm1.where(LoginMember.class).equalTo("Expired", false).findFirst();
            if (loginMember != null) {
                loginMember.Expired = expired;
            }
        });
    }

    public LoginMember findLoginMember() {
        return realm.where(LoginMember.class).findAllAsync().first();
    }

    public LiveRealmData<LoginMember> findLoginMemberLiveRealmData() {
        return asLiveData(realm.where(LoginMember.class).findAllAsync());
    }

    public LiveRealmData<LoginMember> findLoginMember(int id) {
        return asLiveData(realm.where(LoginMember.class).equalTo("id", id).findAllAsync());
    }
}
