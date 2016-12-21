package raijin.myapplication;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by 1918 on 13-Dec-16.
 */

public class DataBaseOpenHelper extends SQLiteAssetHelper {

    public static final String DB_NAME = "city.db.sqlite";
    public static final int DB_VERSION = 1;

    public DataBaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
