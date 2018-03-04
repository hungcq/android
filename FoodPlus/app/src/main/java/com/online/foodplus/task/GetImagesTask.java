package com.online.foodplus.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.online.foodplus.utils.ImageStorage;

import java.net.URL;
import java.net.URLConnection;


/**
 * http://stackoverflow.com/questions/15549421/how-to-download-and-save-an-image-in-android
 */

public class GetImagesTask extends AsyncTask<Object, Object, Object> {
    private String requestUrl, imagename_;
    private Bitmap bitmap;

    public GetImagesTask(String requestUrl, String _imagename_) {
        this.requestUrl = requestUrl;
        this.imagename_ = _imagename_;
    }

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            URL url = new URL(requestUrl);
            URLConnection conn = url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (!ImageStorage.checkifImageExists(imagename_)) {
            ImageStorage.saveToSdCard(bitmap, imagename_);
        }
    }
}
