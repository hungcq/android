package raijin.myapplication;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;

/**
 * Created by 1918 on 16-Sep-16.
 */
public class Res2 extends Resources {

    public static int color = Color.parseColor("#4a148c");
    public Res2 (Resources original) {
        super(original.getAssets(), original.getDisplayMetrics(), original.getConfiguration());
    }

    @Override public int getColor(int id) throws NotFoundException {
        return getColor(id, null);
    }

    @Override public int getColor(int id, Theme theme) throws NotFoundException {
        switch (getResourceEntryName(id)) {
            case "colorPrimary":
                // You can change the return value to an instance field that loads from SharedPreferences.
                return color; // used as an example. Change as needed.
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return super.getColor(id, theme);
                }
                return super.getColor(id);
        }
    }

    @Override
    public float getDimension(int id) throws NotFoundException {
        switch (id) {
            case R.dimen.normalText:
                return getDimension(R.dimen.largeText);
            default:
                return super.getDimension(id);
        }
    }

    get
}
