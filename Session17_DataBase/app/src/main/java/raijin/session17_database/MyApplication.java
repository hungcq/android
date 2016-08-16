package raijin.session17_database;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by 1918 on 14-Aug-16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        realmConfig();
    }

    private void realmConfig() {
        RealmConfiguration configuration = new RealmConfiguration.Builder(getApplicationContext())
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
