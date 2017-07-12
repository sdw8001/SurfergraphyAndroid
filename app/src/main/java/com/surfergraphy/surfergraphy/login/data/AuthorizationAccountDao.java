package com.surfergraphy.surfergraphy.login.data;

import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.asLiveData;

public class AuthorizationAccountDao {

    private Realm realm;

    public AuthorizationAccountDao(Realm realm) {
        this.realm = realm;
    }

    public AuthorizationAccountUser createOrUpdate(AuthorizationAccountUser authorizationAccountUser) {
        if (authorizationAccountUser != null) {
            authorizationAccountUser = realm.copyToRealmOrUpdate(authorizationAccountUser);
        }
        return authorizationAccountUser;
    }

    public void addAccessToken(final String email, final boolean hasRegistered) {
        AuthorizationAccountUser token = new AuthorizationAccountUser(email, hasRegistered);
        realm.insert(token);
    }

    public void deleteAuthorizationAccountUser() {
        realm.delete(AuthorizationAccountUser.class);
    }

    public LiveRealmData<AuthorizationAccountUser> findAuthorizationAccountUser() {
        return asLiveData(realm.where(AuthorizationAccountUser.class).findAllAsync());
    }
}
