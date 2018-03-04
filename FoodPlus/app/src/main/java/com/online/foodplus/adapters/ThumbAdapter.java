package com.online.foodplus.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.online.foodplus.fragments.ThumbFragment;

import java.util.ArrayList;


/**
 * Created by thanhthang on 28/11/2016.
 */

public class ThumbAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> listItems;

    public ThumbAdapter(FragmentManager fm, ArrayList<String> listItems) {
        super(fm);
        this.listItems = listItems;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("image", listItems.get(position));
        Fragment fragment = new ThumbFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }
}
