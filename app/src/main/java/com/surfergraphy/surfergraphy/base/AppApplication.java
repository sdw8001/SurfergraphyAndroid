package com.surfergraphy.surfergraphy.base;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.surfergraphy.surfergraphy.utils.TypefaceUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        // Font 적용
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "SourceSansPro-Bold.ttf");
    }
}
