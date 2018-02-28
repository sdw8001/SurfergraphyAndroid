package com.surfergraphy.surf.login.data;

import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surf.utils.RealmUtils.asLiveData;

public class AuthorizationAccountUserDao {

    private Realm realm;

    public AuthorizationAccountUserDao(Realm realm) {
        this.realm = realm;
    }

    public AuthorizationAccountUser createOrUpdate(AuthorizationAccountUser authorizationAccountUser) {
        if (authorizationAccountUser != null) {
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(authorizationAccountUser));
        }
        return authorizationAccountUser;
    }

    public void deleteAuthorizationAccountUser() {
        realm.executeTransactionAsync(realm1 -> realm1.delete(AuthorizationAccountUser.class));
    }

    public AuthorizationAccountUser findAuthorizationAccountUser() {
        return realm.where(AuthorizationAccountUser.class).findFirst();
    }

    public LiveRealmData<AuthorizationAccountUser> findAuthorizationAccountUserLiveData() {
        return asLiveData(realm.where(AuthorizationAccountUser.class).findAll());
    }
}
