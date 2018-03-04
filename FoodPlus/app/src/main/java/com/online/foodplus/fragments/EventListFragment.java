package com.online.foodplus.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.R;
import com.online.foodplus.activities.EventActivity;
import com.online.foodplus.adapters.EventAdapter;
import com.online.foodplus.libraries.GridSpacingItemDecoration;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.models.Base;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by thanhthang on 14/02/2017.
 */

public class EventListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private RecyclerView rvEvent;
    private EventAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Base> datas;
    private int p = 0;
    private boolean isSending = false;
    private boolean isMoreDataAvailable = true;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageView imgToolbarBack, imgToolbarMenu;
    private TextView tvToolbarTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        init(view);
        listener();
        initData();
        getData();
        return view;
    }

    private void init(View view) {
        imgToolbarBack = (ImageView) view.findViewById(R.id.imgToolbarBack);
        imgToolbarMenu = (ImageView) view.findViewById(R.id.imgToolbarMenu);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        rvEvent = (RecyclerView) view.findViewById(R.id.rvEvent);
    }

    private void listener() {
        swipeRefreshLayout.setOnRefreshListener(this);

        imgToolbarBack.setOnClickListener(this);
        imgToolbarMenu.setOnClickListener(this);
    }

    private void initData() {
        tvToolbarTitle.setText(getResources().getString(R.string.su_kien_extended));

        datas = new ArrayList<>();
        adapter = new EventAdapter(datas);
        layoutManager = new LinearLayoutManager(getActivity());
        rvEvent.setLayoutManager(layoutManager);
        rvEvent.addItemDecoration(new GridSpacingItemDecoration(1, (int) getResources().getDimension(R.dimen._2sdp), false));
        //Animation
        rvEvent.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        rvEvent.setAdapter(scaleAdapter);
        rvEvent.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Fragment fragment = new EventDetailFragment(); // replace your custom fragment class
                Bundle bundle = new Bundle();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                bundle.putString("id", datas.get(position).getId()); // use as per your need
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack("EventList");
                fragmentTransaction.add(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        }));
        rvEvent.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        //Refresh
        TypedValue typed_value = new TypedValue();
        getActivity().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typed_value, true);
        swipeRefreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.Red), ContextCompat.getColor(getActivity(), R.color.Green), ContextCompat.getColor(getActivity(), R.color.Yellow), ContextCompat.getColor(getActivity(), R.color.Blue));

    }

    private void getData() {
        getData(false);
    }

    private void getData(final boolean isRefresh) {
        if (!isSending && (isMoreDataAvailable || isRefresh)) {
            isSending = true;
            p++;
            RequestParams params = new RequestParams();
            params.put("src", "200x200");
            params.put("p", p);
            Tool.get(getResources().getString(R.string.api_eventsrv), params, new JsonHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    if (isAdded())
                        ToastCustom.show(getActivity(), getResources().getString(R.string.please_retry_later), ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if (isAdded())
                        ToastCustom.show(getActivity(), getResources().getString(R.string.please_retry_later), ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onFinish() {
                    isSending = false;
                    if (swipeRefreshLayout.isShown())
                        swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if (isRefresh)
                        datas.clear();

                    int length = response.length();
                    Base base;
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            try {
                                String id = Tool.getStringJson("id", response.getJSONObject(i));
                                String title = Tool.getStringJson("te", response.getJSONObject(i));
                                String description = Tool.getStringJson("mota", response.getJSONObject(i));
                                String datetime = Tool.getStringJson("ngaytao", response.getJSONObject(i));

                                JSONArray imagesArray = response.getJSONObject(i).getJSONArray("images");
                                ArrayList<String> images = new ArrayList<>();
                                if (imagesArray.length() > 0) {
                                    for (int j = 0; j < imagesArray.length(); j++)
                                        images.add(imagesArray.getString(j));
                                }
                                base = new Base();
                                base.setId(id);
                                base.setImages(images);
                                base.setTitle(title);
                                base.setDescription(description);
                                base.setDatetime(datetime);
                                if (i == 0)
                                    base.setType("big");
                                datas.add(base);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if (isAdded())
                            adapter.notifyDataSetChanged();

                    } else
                        isMoreDataAvailable = false;
                }
            });
        }

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        p = 0;
        getData(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgToolbarBack:
                getActivity().onBackPressed();
                break;
            case R.id.imgToolbarMenu:
                ((EventActivity) getActivity()).showDrawable();
                break;
        }
    }
}
