package raijin.chapter5_fragmentcommunication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 1918 on 08-Sep-16.
 */
public class DetailFragment extends Fragment {

    public static String KEY_COUNTRY_NAME = "KEY_COUNTRY_NAME";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(KEY_COUNTRY_NAME)) {
            showSelectedCountry(bundle.getString(KEY_COUNTRY_NAME));
        }
    }

    public void showSelectedCountry(String countryName) {
        ((TextView) getView().findViewById(R.id.textViewCountryName)).setText(countryName);
    }
}
