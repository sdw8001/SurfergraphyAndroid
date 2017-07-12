package com.surfergraphy.surfergraphy.login.data;

import com.surfergraphy.surfergraphy.utils.LiveRealmData;

import io.realm.Realm;
import io.realm.RealmResults;

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

    public void deleteAccessToken() {
        realm.delete(AccessToken.class);
    }

    public void updateExpiredAccessToken(boolean expired) {
        realm.executeTransactionAsync(realm1 -> {
            AccessToken accessToken = realm1.where(AccessToken.class).equalTo("expired", false).findFirst();
            if (accessToken != null) {
                accessToken.expired = expired;
            }
        });
    }

    public LiveRealmData<AccessToken> findAccessToken() {
        return asLiveData(realm.where(AccessToken.class).findAllAsync());
    }

    public LiveRealmData<AccessToken> findAccessToken(String userName) {
        return asLiveData(realm.where(AccessToken.class).equalTo("userName", userName).findAllAsync());
    }
}
