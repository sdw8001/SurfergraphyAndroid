package com.surfergraphy.surf.base;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.surfergraphy.surf.base.typeface.Typekit;
import com.surfergraphy.surf.utils.CommonTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("KEY_HASH", CommonTask.getKeyHash(this));
        AndroidThreeTen.init(this);
        Realm.init(this);
//        RealmConfiguration config = new RealmConfiguration.Builder().build();
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

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
