package raijin.chapter6_databaseloader;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by 1918 on 25-Sep-16.
 */

public class DictionaryLoader extends android.support.v4.content.CursorLoader {

    Context mContext;

    public DictionaryLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Cursor loadInBackground() {
        DictionaryDatabase db = new DictionaryDatabase(mContext);
        return db.getWordList();
    }
}
