package raijin.taxi69.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import raijin.taxi69.fragments.ListTaxiFragment;

/**
 * Created by 1918 on 26-Dec-16.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ListTaxiFragment listTaxiFragment = new ListTaxiFragment();
        listTaxiFragment.setPosition(position);
        return listTaxiFragment;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
