package com.online.foodplus.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.R;
import com.online.foodplus.activities.ShopActivity;
import com.online.foodplus.adapters.GridAdapter;
import com.online.foodplus.libraries.GridSpacingItemDecoration;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.models.Base;
import com.online.foodplus.utils.Tool;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentFragment extends Fragment {
    private RecyclerView rvPopular;
    private ArrayList<Base> datas;
    private GridAdapter adapter;
    private GridLayoutManager layoutManager;
    private boolean isSending = false;
    private boolean isMoreDataAvailable = true;
    private int p = 0;
    private RequestParams params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        init(view);
        initValue();
        getData();
        return view;
    }

    private void init(View view) {
        rvPopular = (RecyclerView) view.findViewById(R.id.rvPopular);
    }

    private void initValue() {
        datas = new ArrayList<>();
        rvPopular.setNestedScrollingEnabled(false);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        rvPopular.setLayoutManager(layoutManager);
        datas = new ArrayList<>();
        adapter = new GridAdapter(datas);
        //Animation
        rvPopular.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        rvPopular.setAdapter(scaleAdapter);

        rvPopular.addItemDecoration(new GridSpacingItemDecoration(2, (int) getActivity().getResources().getDimension(R.dimen._4sdp), false));
        rvPopular.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ShopActivity.class);
                intent.putExtra("id", datas.get(position).getId());
                intent.putExtra("t", datas.get(position).getTid());
                intent.putExtra("cid", datas.get(position).getCid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        }));
        rvPopular.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //check for scroll down
                if (isMoreDataAvailable)
                    if (!isSending)
                        if (dy > 0)
                            if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.getItemCount())
                                getData();                                //Do pagination.. i.e. fetch new data
            }
        });

        params = new RequestParams();
        params.put("long", 105.882384);
        params.put("lat", 21.02);
        params.put("t", 1);
        params.put("scr", "400x400");
        params.put("p", p);
        String cityID = Tool.getCityId(getActivity());
        if (!cityID.equals(""))
            params.put("city", cityID);
    }

    private void getData() {
        if (!isSending && isMoreDataAvailable) {
            isSending = true;
            p++;
            params.put("p", p);                             //p thay đổi
            String cityID = Tool.getCityId(getActivity());       //cityID có thể thay đổi
            if (!cityID.equals(""))
                params.put("city", cityID);
            //System.out.println("------------------------RECENT REQUEST " + getResources().getString(R.string.api_mapsrv) + "?" + params.toString());

            Tool.get(getResources().getString(R.string.api_mapsrv), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    //System.out.println("------------------------RECENT RESPONSE " + getResources().getString(R.string.api_mapsrv) + "?" + params.toString());

                    int length = response.length();
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            try {
                                datas.add(Tool.readObject(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        //result size 0 means there is no more data available at server
                        isMoreDataAvailable = false;
                        //ToastCustom.show(getActivity(), "Hết dữ liệu", Toast.LENGTH_LONG, ToastCustom.WARNING);
                    }

                }

                @Override
                public void onFinish() {
                    isSending = false;
                }
            });
        }

    }

}
