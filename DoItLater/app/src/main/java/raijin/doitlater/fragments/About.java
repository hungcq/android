package raijin.doitlater.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import raijin.doitlater.R;
import raijin.doitlater.managers.ScreenManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class About extends Fragment {


    public static About create(ScreenManager screenManager) {
        About about = new About();
        return about;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("About");
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

}
