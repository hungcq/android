package raijin.taxi69.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import raijin.taxi69.Constants;
import raijin.taxi69.R;
import raijin.taxi69.adapters.RecyclerViewListTaxiAdapter;
import raijin.taxi69.models.TaxiType;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListTaxiFragment extends Fragment {

    private DatabaseReference databaseReference;
    private List<TaxiType> category1List;
    private List<TaxiType> category2List;
    private List<TaxiType> category3List;
    private RecyclerView recyclerView;
    private int position;


    public ListTaxiFragment() {
        // Required empty public constructor
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_taxi, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_taxi);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        category1List = new ArrayList<>();
        category2List = new ArrayList<>();
        category3List = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_TYPE);
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                TaxiType taxiType = dataSnapshot.getValue(TaxiType.class);
//                if (taxiType.getCategory() == 1) {
//                    category1List.add(taxiType);
//                    Log.d("DMM", category1List.size() + "");
//                } else if (taxiType.getCategory() == 2) {
//                    category2List.add(taxiType);
//                } else category3List.add(taxiType);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    TaxiType taxiType = child.getValue(TaxiType.class);
                    if (taxiType.getCategory() == 1) {
                        category1List.add(taxiType);
                    } else if (taxiType.getCategory() == 2) {
                        category2List.add(taxiType);
                    } else category3List.add(taxiType);
                }
                switch (position) {
                    case 0:
                        recyclerView.setAdapter(new RecyclerViewListTaxiAdapter(category1List));
                        break;
                    case 1:
                        recyclerView.setAdapter(new RecyclerViewListTaxiAdapter(category2List));
                        break;
                    case 2:
                        recyclerView.setAdapter(new RecyclerViewListTaxiAdapter(category3List));
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
}
