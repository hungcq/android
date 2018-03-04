package com.online.foodplus.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.online.foodplus.fragments.UserInfoFragment;
import com.online.foodplus.fragments.UserMarkFragment;
import com.online.foodplus.fragments.UserRecipeFragment;

/**
 * Created by 1918 on 27-Dec-16.
 */

public class ActivityUserViewPagerAdapter extends FragmentPagerAdapter {

    public ActivityUserViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new UserInfoFragment();
                break;
            case 1:
                fragment = new UserRecipeFragment();
                break;
            case 2:
                fragment = new UserMarkFragment();
                break;
            default:
                fragment = new UserInfoFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        //        return 3;
        return 1;
    }
}
