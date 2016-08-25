package raijin.session21_sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by 1918 on 23-Aug-16.
 */
public class DatabaseOpenHelper extends SQLiteAssetHelper {
    public static final String DB_NAME = "data.db";
    public static final int DB_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
