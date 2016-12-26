package raijin.taxi69.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import raijin.taxi69.Constants;
import raijin.taxi69.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListTaxiFragment extends Fragment {

    private DatabaseReference databaseReference;


    public ListTaxiFragment() {
        // Required empty public constructor
    }

    public void setPosition(int position) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_taxi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_TYPE);
        databaseReference.
    }
}
