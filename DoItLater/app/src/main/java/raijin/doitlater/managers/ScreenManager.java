package raijin.doitlater.managers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import raijin.doitlater.fragments.AllTasksFragment;
import raijin.doitlater.fragments.CompletedFragment;
import raijin.doitlater.fragments.ToDoFragment;

/**
 * Created by Qk Lahpita on 7/23/2016.
 */
public class ScreenManager {
    private FragmentManager fragmentManager;
    private int fragmentContainerId;
    private static FragmentType currentFragment = FragmentType.TODO;
    public static boolean isOpeningMap = false;

    public ScreenManager(FragmentManager fragmentManager, int fragmentContainerId) {
        this.fragmentManager = fragmentManager;
        this.fragmentContainerId = fragmentContainerId;
    }

    public static FragmentType getCurrentFragment() {
        return currentFragment;
    }

    public static void setCurrentFragment(FragmentType currentFragment) {
        ScreenManager.currentFragment = currentFragment;
    }

    public void openFragment(Fragment fragment, boolean addToBackStack) {
        if (fragment instanceof AllTasksFragment) {
            currentFragment = FragmentType.ALLTASK;
        } else if (fragment instanceof CompletedFragment) {
            currentFragment = FragmentType.COMPLETED;
        } else if (fragment instanceof ToDoFragment) {
            currentFragment = FragmentType.TODO;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentContainerId, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commit();
    }
}
