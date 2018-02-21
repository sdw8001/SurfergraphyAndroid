package com.surfergraphy.surfergraphy.base;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.surfergraphy.surfergraphy.base.typeface.Typekit;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        Realm.init(this);
//        RealmConfiguration config = new RealmConfiguration.Builder().build();
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        // Font 적용
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "SourceSansPro-Regular.otf"))
                .addItalic(Typekit.createFromAsset(this, "SourceSansPro-It.otf"))
                .addLight(Typekit.createFromAsset(this, "SourceSansPro-Light.otf"))
                .addLightItalic(Typekit.createFromAsset(this, "SourceSansPro-LightIt.otf"))
                .addBold(Typekit.createFromAsset(this, "SourceSansPro-Bold.otf"))
                .addBoldItalic(Typekit.createFromAsset(this, "SourceSansPro-BoldIt.otf"))
                .addBlack(Typekit.createFromAsset(this, "SourceSansPro-Black.otf"))
                .addBlackItalic(Typekit.createFromAsset(this, "SourceSansPro-BlackIt.otf"));
    }
}
