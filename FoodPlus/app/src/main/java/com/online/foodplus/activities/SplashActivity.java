package com.online.foodplus.activities;

import android.content.Intent;
import android.os.Bundle;

import com.online.foodplus.R;

/**
 * Created by 1918 on 21-Nov-16.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
