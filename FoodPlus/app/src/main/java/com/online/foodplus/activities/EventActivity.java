package com.online.foodplus.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.online.foodplus.R;
import com.online.foodplus.fragments.EventListFragment;

/**
 * Created by thanhthang on 29/12/2016.
 */

public class EventActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //initToolbarSimple("Thông báo");
        setUpDrawerLayout(R.id.nav_alarm_item);

        displayView(0);
    }

    private void displayView(int i) {
        Fragment fragment = null;
        if (i == 0) {
            fragment = new EventListFragment();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        }

    }

}
