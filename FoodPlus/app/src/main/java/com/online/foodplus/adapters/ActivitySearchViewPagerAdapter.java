package com.online.foodplus.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.online.foodplus.fragments.NearbyFragment;
import com.online.foodplus.fragments.PopularFragment;
import com.online.foodplus.fragments.RecentFragment;

/**
 * Created by 1918 on 03-Jan-17.
 */

public class ActivitySearchViewPagerAdapter extends FragmentStatePagerAdapter {

    public ActivitySearchViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new NearbyFragment();
                break;
            case 1:
                fragment = new RecentFragment();
                break;
            case 2:
                fragment = new PopularFragment();
                break;
            default:
                fragment = new PopularFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
