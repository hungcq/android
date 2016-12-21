package raijin.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 1918 on 13-Dec-16.
 */

public class DBContext {

    private SQLiteDatabase database;
    private SQLiteOpenHelper sqLiteOpenHelper;

    private DBContext(Context context) {
        this.sqLiteOpenHelper = new DataBaseOpenHelper(context);
    }

    private static DBContext inst;

    public static DBContext getInst(Context context) {
        if (inst == null) {
            inst = new DBContext(context);
        }
        return inst;
    }

    public void openConnection() {
        database = sqLiteOpenHelper.getWritableDatabase();
    }

    public void closeConnection() {
        if (database != null) {
            database.close();
        }
    }

    public void addRecord(int ID, String name, String ascName, int zipCode, int status, int countryCode) {
        database = sqLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", ID);
        values.put("ten", name);
        values.put("ten_asc", ascName);
        values.put("zipcode", zipCode);
        values.put("trangthai", status);
        values.put("maquocgia", countryCode);
        database.insert("city", null, values);
    }
}
