package raijin.session21_hw;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1918 on 23-Aug-16.
 */
public class DBContext {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase database;

    private DBContext(Context context) {
        this.sqLiteOpenHelper = new DatabaseOpenHelper(context);
    }

    private static DBContext inst;

    public static DBContext getInst(Context context) {
        if (inst == null) {
            inst = new DBContext(context);
        }
        return inst;
    }

    public void openConnection() {
        database = sqLiteOpenHelper.getReadableDatabase();
    }

    public void closeConnection() {
        if (database != null) {
            database.close();
        }
    }


    public List<MathWord> getAllMathWord() {
        List<MathWord> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT id, subject, a, b, c, d, test, explain FROM mathword", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            List<String> listAnswer = new ArrayList<>();

            int id = Integer.parseInt(cursor.getString(0));
            String questionContent = cursor.getString(1);
            listAnswer.add(cursor.getString(2));
            listAnswer.add(cursor.getString(3));
            listAnswer.add(cursor.getString(4));
            listAnswer.add(cursor.getString(5));
            int result = Integer.parseInt(cursor.getString(6));
            String expalanation = cursor.getString(7);

            list.add(new MathWord(id, questionContent, listAnswer, result, expalanation));

            cursor.moveToNext();
        }
        return list;
    }

    public MathWord getMathWord(int position) {
        String positionString = Integer.toString(position + 1);
        Cursor cursor = database.rawQuery("SELECT id, subject, a, b, c, d, test, explain FROM mathword WHERE id = " + positionString, null);
        cursor.moveToFirst();
        List<String> listAnswer = new ArrayList<>();

        int id = Integer.parseInt(cursor.getString(0));
        String questionContent = cursor.getString(1);
        listAnswer.add(cursor.getString(2));
        listAnswer.add(cursor.getString(3));
        listAnswer.add(cursor.getString(4));
        listAnswer.add(cursor.getString(5));
        int result = Integer.parseInt(cursor.getString(6));
        String explanation = cursor.getString(7);

        return new MathWord(id, questionContent, listAnswer, result, explanation);

    }
}
