package com.online.foodplus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.online.foodplus.R;
import com.online.foodplus.adapters.GridSimpleAdapter;
import com.online.foodplus.models.Base;

import java.util.ArrayList;


/**
 * Created by thanhthang on 25/11/2016.
 */

public class UserRecipeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Base> datas;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user_recipe, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        datas = new ArrayList<>();
        GridSimpleAdapter gridSimpleAdapter = new GridSimpleAdapter(datas);
        recyclerView.setAdapter(gridSimpleAdapter);
    }
}
