package raijin.doitlater;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import raijin.doitlater.activities.MainActivity;

/**
 * Created by Qk Lahpita on 7/23/2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPref.getString(MainActivity.SettingsFragment.PREF_KEY_THEME_COLOR, "");
        if (theme.equals("theme_default")) {
            setTheme(R.style.AppTheme);
        } else if (theme.equals("theme_teal")) {
            setTheme(R.style.TealTheme);
        } else if (theme.equals("theme_purple")) {
            setTheme(R.style.PurpleTheme);
        } else if (theme.equals("theme_grey")) {
            setTheme(R.style.GreyTheme);
        } else if (theme.equals("theme_orange")) {
            setTheme(R.style.OrangeTheme);
        } else if (theme.equals("theme_pink")) {
            setTheme(R.style.PinkTheme);
        }
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getApplicationContext())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
