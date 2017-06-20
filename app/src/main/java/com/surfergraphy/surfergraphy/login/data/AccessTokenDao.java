package com.surfergraphy.surfergraphy.login.data;

import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.asLiveData;

public class AccessTokenDao {

    private Realm realm;

    public AccessTokenDao(Realm realm) {
        this.realm = realm;
    }

    public AccessToken createOrUpdate(AccessToken accessToken) {
        if (accessToken != null) {
            accessToken = realm.copyToRealmOrUpdate(accessToken);
        }
        return accessToken;
    }

    public void addAccessToken(final String userName, final String accessToken, final String tokenType, final int expireTimeSeconds) {
        AccessToken token = new AccessToken(userName, accessToken, tokenType, expireTimeSeconds);
        realm.insert(token);
    }

    public LiveRealmData<AccessToken> findAccessToken() {
        return asLiveData(realm.where(AccessToken.class).findAllAsync());
    }
}
