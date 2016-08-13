package raijin.session16_weatherapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 1918 on 13-Aug-16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter{

    private static int NUMBER_PAGE = 3;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setCity(position);
        return detailsFragment;
    }

    @Override
    public int getCount() {
        return NUMBER_PAGE;
    }
}
