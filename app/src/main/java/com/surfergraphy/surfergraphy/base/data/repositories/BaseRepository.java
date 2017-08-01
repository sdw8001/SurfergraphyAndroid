package com.surfergraphy.surfergraphy.base.data.repositories;

import com.surfergraphy.surfergraphy.base.data.ActionResponse;

import io.realm.Realm;

import static com.surfergraphy.surfergraphy.utils.RealmUtils.actionResponseModel;

public class BaseRepository {
    protected Realm realm;

    public BaseRepository(final Realm realm) {
        if (realm == null) {
            this.realm = Realm.getDefaultInstance();
        } else {
            this.realm = realm;
        }
    }

    protected ActionResponse createOrUpdateActionResponse(final ActionResponse actionResponse) {
        return actionResponseModel(realm).createOrUpdate(actionResponse);
    }
}
