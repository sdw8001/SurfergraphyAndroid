package com.surfergraphy.surfergraphy.login.data;

import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.asLiveData;

public class AuthorizationAccountUserDao {

    private Realm realm;

    public AuthorizationAccountUserDao(Realm realm) {
        this.realm = realm;
    }

    public AuthorizationAccountUser createOrUpdate(AuthorizationAccountUser authorizationAccountUser) {
        if (authorizationAccountUser != null) {
            authorizationAccountUser = realm.copyToRealmOrUpdate(authorizationAccountUser);
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
        return asLiveData(realm.where(AuthorizationAccountUser.class).findAllAsync());
    }
}
