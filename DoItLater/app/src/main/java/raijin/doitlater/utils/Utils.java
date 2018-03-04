package raijin.doitlater.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import raijin.doitlater.R;
import raijin.doitlater.activities.MainActivity;

/**
 * Created by Qk Lahpita on 8/29/2016.
 */
public class Utils {
    public static String TIME_FORMAT = "HH:mm";
    public static String DATE_FORMAT = "dd-MM-yyyy";
    public static String TIME_FORMAT_STRING = "%02d:%02d";
    public static String DATE_FORMAT_STRING = "%02d-%02d-%04d";


    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(TIME_FORMAT);
        return df.format(cal.getTime());
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(cal.getTime());
    }

    public static String getDateString(int year, int month, int dayOfMonth) {
        return String.format(DATE_FORMAT_STRING, dayOfMonth, month + 1, year);
    }

    public static String getTimeString(int hour, int minute) {
        return String.format(TIME_FORMAT_STRING, hour, minute);
    }

    public static String getDateReminder(String dateReminder) {
        return dateReminder.substring(10, 20);
    }

    public static String getTimeReminder(String timeReminder) {
        return timeReminder.substring(22, timeReminder.length());
    }

    public static int getDayFromDateReminder(String dateReminder) {
        String strArrtmp[] = dateReminder.split("-");
        return Integer.parseInt(strArrtmp[0]);
    }

    public static int getMonthFromDateReminder(String dateReminder) {
        String strArrtmp[] = dateReminder.split("-");
        return Integer.parseInt(strArrtmp[1]);
    }

    public static int getYearFromDateReminder(String dateReminder) {
        String strArrtmp[] = dateReminder.split("-");
        return Integer.parseInt(strArrtmp[2]);
    }

    public static int getHourFromTimeReminder(String timeReminder) {
        String strArrtmp[] = timeReminder.split(":");
        return Integer.parseInt(strArrtmp[0]);
    }

    public static int getMinuteFromTimeReminder(String timeReminder) {
        String strArrtmp[] = timeReminder.split(":");
        return Integer.parseInt(strArrtmp[1]);
    }

    public static Bitmap decodeImageFile(String imagePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 200;

        // Find the correct scale value. It should be the power of 2.
        int scale = 1;
        while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, o2);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap bmRotated = rotateBitmap(bitmap, orientation);
        return bmRotated;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setTheme(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String theme = settings.getString(MainActivity.SettingsFragment.PREF_KEY_THEME_COLOR, "");
        if (theme.equals("theme_default")) {
            context.setTheme(R.style.AppTheme);
        } else if (theme.equals("theme_teal")) {
            context.setTheme(R.style.TealTheme);
        } else if (theme.equals("theme_purple")) {
            context.setTheme(R.style.PurpleTheme);
        } else if (theme.equals("theme_grey")) {
            context.setTheme(R.style.GreyTheme);
        } else if (theme.equals("theme_orange")) {
            context.setTheme(R.style.OrangeTheme);
        } else if (theme.equals("theme_pink")) {
            context.setTheme(R.style.PinkTheme);
        }
    }
}
