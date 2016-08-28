package com.example.linhdq.sessiondbrealm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by linhdq on 8/14/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        realmConfig();
    }

    private void realmConfig() {
        RealmConfiguration configuration =
                new RealmConfiguration.Builder(getApplicationContext())
                        .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
