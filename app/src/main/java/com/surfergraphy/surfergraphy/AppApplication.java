package com.surfergraphy.surfergraphy;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ddfactory on 2017-06-13.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }
}
