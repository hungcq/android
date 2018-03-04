package com.online.foodplus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.models.Base;
import com.online.foodplus.models.Display;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;

/**
 * Created by thanhthang on 07/12/2016.
 */

public class Tool {
    private static AsyncHttpClient client = new AsyncHttpClient();

    private static void initSSL(String url) {
        if (url.trim().toLowerCase().startsWith("https://")) {
            try {
                /// We initialize a default Keystore
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

                // We load the KeyStore
                trustStore.load(null, null);
                // We initialize a new SSLSocketFacrory
                MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
                // We set that all host names are allowed in the socket factory
                socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                // We initialize the Async Client
                client.setSSLSocketFactory(socketFactory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        initSSL(url);
        client.get(url, params, responseHandler);

    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        initSSL(url);
        client.post(url, params, responseHandler);
    }

    public static boolean isLogin(Context context) {
        String uid = getUserId(context);
        return !uid.equals("");
    }

    public static String getUserId(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("user_id", "");
    }

    public static String getUsername(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", "");
    }

    public static String getEmail(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("email", "");
    }

    public static String getPhone(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("phone", "");
    }

    public static String getGender(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("gender", "0");
    }

    public static String getAvatar(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("avatar", "");
    }

    public static String getCityId(Context context) {
        return context.getSharedPreferences("data", Context.MODE_PRIVATE).getString("city_id", "");
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String getStringJson(String key, JSONObject joData) {
        try {
            if (joData.has(key))
                return (joData.getString(key) != null) ? joData.getString(key) : null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Integer getIntJson(String key, JSONObject joData) {
        try {
            return joData.has(key) ? joData.getInt(key) : 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String getHashToken(String input) {
        return Md5.Hash(input).substring(0, 20);
    }

    public static Display readDisplay(JSONObject data) {
        Display display = new Display();
        try {
            //            if (!data.has("type")) //Kiểm tra khóa Type trước, (nếu ko có thì next)
            //                return null;
            String type = getStringJson("type", data);
            String titlebox = getStringJson("title", data);
            String icon = getStringJson("icon", data);
            String total = getStringJson("total", data);
            if (data.has("object"))
                display.setObject(readObject(data.getJSONObject("object")));

            if (data.has("data")) {
                JSONArray jaData = data.getJSONArray("data");
                int lengthData = jaData.length();
                ArrayList<Base> datas = new ArrayList<>();
                for (int j = 0; j < lengthData; j++)
                    datas.add(readObject(jaData.getJSONObject(j)));

                display.setData(datas);
            }
            if (data.has("images")) {
                JSONArray jaImages = data.getJSONArray("images");
                ArrayList<String> images = new ArrayList<>();
                for (int x = 0; x < jaImages.length(); x++)
                    images.add(jaImages.getString(x));
                display.setImages(images);
            }
            display.setTitle(titlebox);
            display.setType(type);
            display.setIcon(icon);
            display.setTotal(total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return display;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  ArrayList<Display> để lưu mỗi phần tử Object trong JsonArray:
    //    {
    //            "type": "titlebox",
    //            "title": "M\u00f3n \u0103n",
    //            "icon": "http:\/\/test.bigvn.net\/idtv\/connect\/images\/placeholder.png"
    //            "images": [...],
    //            "data": [... ]
    //    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static ArrayList<Display> readData(JSONArray response) {
        if (response == null)
            return null;
        ArrayList<Display> mains = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {
            Display display;
            try {
                display = readDisplay(response.getJSONObject(i));
                if (display != null)
                    mains.add(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mains;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //   Hàm này để đọc Object có kiểu đối tượng Base trong JSONArray trên
    //    {
    //            "id": 720,
    //            "title": "Ruốc cá hồi Nauy hộp 100g",
    //            "description": "Số 46 Nguyễn Thị Định, Trung Hòa, Cầu Giấy, Hà Nội",
    //            "comment": 0,
    //            "pin": 0,
    //            "favourite": 0,
    //            "rating": 0,
    //            "star": 0,
    //            "images": [
    //            "http://dev.xplay.vn/img/2017/01/43/720_1_200x200.jpg"
    //            ],
    //            "maloai": 301,
    //            "cid": 43,
    //            "tid": 1
    //            "feature": "http:\/\/test.bigvn.net\/idtv\/connect\/images\/placeholder.png"
    //    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static Base readObject(JSONObject joData) {
        Base base = new Base();

        String id = String.valueOf(getIntJson("id", joData));
        String cid = String.valueOf(getIntJson("cid", joData));
        String t = String.valueOf(getIntJson("tid", joData));
        String feature = getStringJson("feature", joData);
        String title = getStringJson("title", joData);
        String description = getStringJson("description", joData);
        String rating = String.valueOf(getIntJson("rating", joData));
        String comment = String.valueOf(getIntJson("comment", joData));
        String pin = String.valueOf(getIntJson("pin", joData));
        String favourite = String.valueOf(getIntJson("favourite", joData));
        String star = String.valueOf(getIntJson("star", joData));
        String baseType = getStringJson("type", joData);
        String content = getStringJson("content", joData);
        try {
            Double longitude = joData.has("longitude") ? joData.getDouble("longitude") : 0;
            base.setLongitude(longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Double latitude = joData.has("latitude") ? joData.getDouble("latitude") : 0;
            base.setLatitude(latitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (joData.has("images")) {
                JSONArray jaImages = joData.getJSONArray("images");
                ArrayList<String> images = new ArrayList<>();
                for (int x = 0; x < jaImages.length(); x++)
                    images.add(jaImages.getString(x));
                base.setImages(images);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        base.setTitle(title);
        base.setId(id);
        base.setCid(cid);
        base.setTid(t);
        base.setPin(pin);
        base.setFeature(feature);
        base.setComment(comment);
        base.setDescription(description);
        base.setRating(rating);
        base.setFavourite(favourite);
        base.setStar(star);
        base.setType(baseType);
        base.setContent(content);

        return base;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static int getScreenHeight(Activity activity) {
        android.view.Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getScreenWidth(Activity activity) {
        android.view.Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static void buildAlertMessageNoGps(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Chưa cho phép truy cập vị trí của bạn. Vào settings để thiết lập lại?")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static String findImageUrl(String str, String size) {
        String SMALL_SIZE = "200x200.";
        String NORMAL_SIZE = "400x400.";
        String BIG_SIZE = "600x600.";
        switch (size) {
            case Constants.IMAGE_BIG:
                return str.replace(SMALL_SIZE, BIG_SIZE).replace(NORMAL_SIZE, BIG_SIZE);
            case Constants.IMAGE_NORMAL:
                return str.replace(SMALL_SIZE, NORMAL_SIZE).replace(BIG_SIZE, NORMAL_SIZE);
            case Constants.IMAGE_SMALL:
                return str.replace(NORMAL_SIZE, SMALL_SIZE).replace(BIG_SIZE, SMALL_SIZE);
            case Constants.IMAGE_ORIGINAL:
                return str.replace(NORMAL_SIZE, "").replace(SMALL_SIZE, "").replace(BIG_SIZE, "");
            case Constants.IMAGE_NOT_CHANGE:
                return str;
        }
        return str;
    }

    public static String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + context.getResources().getString(R.string.facebook_fanpage_url);
            } else { //older versions of fb app
                return "fb://page/" + context.getResources().getString(R.string.facebook_fanpage_id);
            }
        } catch (PackageManager.NameNotFoundException e) {
            return context.getResources().getString(R.string.facebook_fanpage_url); //normal web url
        }
    }
}
