package com.surfergraphy.surf.base.data;

import com.surfergraphy.surf.utils.LiveRealmData;

import io.realm.Realm;

import static com.surfergraphy.surf.utils.RealmUtils.asLiveData;

public class ActionResponseDao {

    private Realm realm;

    public ActionResponseDao(Realm realm) {
        this.realm = realm;
    }

    public ActionResponse createOrUpdate(ActionResponse actionResponse) {
        realm.beginTransaction();
        if (actionResponse != null) {
            actionResponse = realm.copyToRealmOrUpdate(actionResponse);
        }
        realm.commitTransaction();
        return actionResponse;
    }

    public LiveRealmData<ActionResponse> findActionResponse(final int actionCode) {
        return asLiveData(realm.where(ActionResponse.class).equalTo("actionCode", actionCode).equalTo("expired", false).findAllAsync());
    }

    public void deleteActionResponse(final int actionCode) {
        realm.beginTransaction();
        realm.where(ActionResponse.class).equalTo("actionCode", actionCode).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }
}
