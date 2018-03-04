package com.online.foodplus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.adapters.CategoryAdapter;
import com.online.foodplus.adapters.SubAdapter;
import com.online.foodplus.libraries.GridSpacingItemDecoration;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.models.Base;
import com.online.foodplus.models.Display;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by thanhthang on 06/12/2016.
 */

public class CategoryActivity extends BaseActivity/* implements SwipeRefreshLayout.OnRefreshListener*/ {
    //Category
    private String categoryTitle="";
    //Request Connect
    private boolean isMoreDataAvailable = true;
    private int p = 0;
    private String t = "1";
    private String cid = "43";
    private String scid = "43";
    private boolean isSubData = false;
    private boolean isSending = false;

    private SliderLayout sliderLayout;
    //private SwipeRefreshLayout swipeRefreshLayout;

    private int checkedItem = 0;
    private static final int NUMBER_ITEM_VIEW_1 = 4;
    private static final int NUMBER_ITEM_VIEW_2 = 3;
    private static final int NUMBER_ITEM_VIEW_3 = 6;
    private static final int NUMBER_ITEM_VIEW_4 = 2;
    private static final int NUMBER_ITEM_TOTAL = NUMBER_ITEM_VIEW_1 + NUMBER_ITEM_VIEW_2
            + NUMBER_ITEM_VIEW_3 + NUMBER_ITEM_VIEW_4;

    private RecyclerView rvCategory;
    private LinearLayoutManager layoutManager;
    private ArrayList<Display> displays;
    private CategoryAdapter adapter;

    private RecyclerView rvSub;
    private ArrayList<Base> subs;
    private SubAdapter subAdapter;

    private ProgressBar pbToolbar;

    private CoordinatorLayout layoutContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        received();
        initToolbarSimple(categoryTitle, true);
        init();
        initNetworkConnectedCheck(layoutContainer);
        initData();
        listener();
        getData(false);
        getSubCategory();
        setUpDrawerLayout(checkedItem);
    }

    private void received() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra("cid"))
                cid = extras.getString("cid", "1");
            if (getIntent().hasExtra("t"))
                t = extras.getString("t", "43");
            if (getIntent().hasExtra("title"))
                categoryTitle = extras.getString("title");

            if (t.equals("1") && cid.equals("43"))
                checkedItem = R.id.nav_food_item;
            else if (t.equals("2") && cid.equals("21"))
                checkedItem = R.id.nav_knife_item;
            else if (t.equals("3") && cid.equals("42"))
                checkedItem = R.id.nav_recipe_item;
            else if (t.equals("1") && cid.equals("41"))
                checkedItem = R.id.nav_coffee_item;
            else if (t.equals("5") && cid.equals("44"))
                checkedItem = R.id.nav_spa_item;
        }
    }

    private void init() {
        pbToolbar = (ProgressBar) findViewById(R.id.pbToolbar);
        layoutContainer = (CoordinatorLayout) findViewById(R.id.layoutContainer);

        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(swipeRefreshLayout);
        sliderLayout = (SliderLayout) findViewById(R.id.slider_layout);
        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        rvSub = (RecyclerView) findViewById(R.id.rvSub);
    }

    private void initData() {
        pbToolbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

        displays = new ArrayList<>();
        adapter = new CategoryAdapter(this, displays);
        layoutManager = new LinearLayoutManager(this);
        rvCategory.setLayoutManager(layoutManager);
        rvCategory.addItemDecoration(new GridSpacingItemDecoration(1, (int) getResources().getDimension(R.dimen._2sdp), false));
        rvCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //check for scroll down
                if (isMoreDataAvailable)
                    if (!isSending)
                        if (dy > 0)
                            if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.getItemCount())
                                getData(false);                                //Do pagination.. i.e. fetch new data
            }
        });
        //Animation
        rvCategory.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        rvCategory.setAdapter(scaleAdapter);

        //Refresh
        //        TypedValue typed_value = new TypedValue();
        //        getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typed_value, true);
        //        swipeRefreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        //        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.Red), ContextCompat.getColor(getApplicationContext(), R.color.Green), ContextCompat.getColor(getApplicationContext(), R.color.Yellow), ContextCompat.getColor(getApplicationContext(), R.color.Blue));

        //Sub
        subs = new ArrayList<>();
        subAdapter = new SubAdapter(subs);
        rvSub.addItemDecoration(new GridSpacingItemDecoration(2
                , (int) getResources().getDimension(R.dimen._4sdp), false));
        rvSub.setLayoutManager(new GridLayoutManager(this, 2));

        //        rvSub.addItemDecoration(new GridHorizontalSpacingItemDecoration((int) getResources().getDimension(R.dimen._4sdp)));
        //        rvSub.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));

        rvSub.setAdapter(subAdapter);
        rvSub.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (subs.get(position).getId().equals("0")) {
                    //System.out.println("-----------------SCID: " + cid);
                    p = 0;
                    isMoreDataAvailable = true;
                    isSubData = false;
                    getData(true);
                } else {
                    scid = subs.get(position).getId();
                    //System.out.println("-----------------SCID: " + cid);
                    p = 0;
                    isMoreDataAvailable = true;
                    isSubData = true;
                    getData(true);

                }
                initToolbarSimple(subs.get(position).getId().equals("0") ? categoryTitle : subs.get(position).getTitle());
                for (int i = 0, size = subs.size(); i < size; i++)
                    subs.get(i).setType(null);
                subs.get(position).setType("active");
                subAdapter.notifyDataSetChanged();
            }
        }));
    }

    private void listener() {
        // swipeRefreshLayout.setOnRefreshListener(this);

    }

    private void getSubCategory() {
        RequestParams params = new RequestParams();
        params.put("t", t);
        params.put("cid", cid);
        Tool.get(getResources().getString(R.string.api_catsrv), params, new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    if (response.getJSONObject(0) != null) {
                        Base subAll = new Base();
                        subAll.setType("active");
                        subAll.setId("0");
                        subAll.setTitle("Toàn bộ");
                        subs.add(subAll);

                        JSONArray jsonArray = response.getJSONObject(0).getJSONArray("loai");
                        int length = jsonArray.length();
                        Base base;
                        for (int i = 0; i < length; i++) {
                            base = new Base();
                            base.setId(String.valueOf(Tool.getIntJson("id", jsonArray.getJSONObject(i))));
                            base.setTitle(Tool.getStringJson("tenloai", jsonArray.getJSONObject(i)));
                            subs.add(base);
                        }
                        rvSub.setVisibility(View.VISIBLE);
                        subAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void getData(final boolean reload) {
        if (!Tool.isNetworkConnected(getApplicationContext())) {
            showNetworkStatusNotification(true);
            return;
        }
        if (!isSending && (isMoreDataAvailable)) {
            isSending = true;
            p++;
            RequestParams params = new RequestParams();
            params.put("t", t);
            if (isSubData)
                params.put("scid", scid);
            else
                params.put("cid", cid);
            params.put("p", p);
            params.put("scr", "400x400");
            // System.out.println("----------------CATEGORY REQUEST: " + getResources().getString(R.string.api_category) + "?" + params.toString());
            Tool.post(getResources().getString(R.string.api_category), params, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    pbToolbar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //System.out.println("----------------CATEGORY RESPONSE: " + response);

                    try {
                        if (reload)
                            displays.clear();
                        if (response.has("content"))
                            updateData(response.getJSONArray("content"));
                        if (p == 1)
                            if (response.has("slide"))
                                initSlider(response.getJSONObject("slide"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    isSending = false;
                    pbToolbar.setVisibility(View.GONE);
                    //                    if (swipeRefreshLayout.isShown())
                    //                        swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            //            if (swipeRefreshLayout.isShown())
            //                swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initSlider(JSONObject slide) throws JSONException {
        if (slide.has("images")) {
            JSONArray images = slide.getJSONArray("images");
            int length = images.length();

            if (length > 4) //Max 4 images in slider
                length = 4;

            if (length > 0) {
                sliderLayout.setVisibility(View.VISIBLE);
                sliderLayout.removeAllSliders();
                for (int i = 0; i < length; i++)
                    sliderLayout.addSlider(new DefaultSliderView(CategoryActivity.this)
                            .image(images.getString(i)).setScaleType(BaseSliderView.ScaleType.CenterCrop));
            }

        }
    }

    private void updateData(JSONArray response) {
        //System.out.println("----------------SUB RESPONSE CONENT: " + response);

        int length = response.length();
        if (length == 0) {
            isMoreDataAvailable = false;      //Dừng request nếu server hết dữ liệu, trả về mảng trống []
            if (p == 1)
                ToastCustom.show(getApplicationContext(), getResources().getString(R.string.no_data), ToastCustom.WARNING, ToastCustom.LENGTH_SHORT);
        }

        int count = 0;
        Display display;
        ArrayList<Base> dataList = new ArrayList<>();
        int currentState;
        int lastState = checkCount(count);
        while (true) {
            if (count > length) break;
            currentState = checkCount(count);
            if (currentState != lastState) {
                if (lastState == 1) {
                    display = new Display();
                    display.setData(dataList);
                    display.setType("grid");
                    displays.add(display);
                } else if (lastState == 2) {
                    display = new Display();
                    display.setData(dataList);
                    display.setType("list");
                    displays.add(display);
                } else if (lastState == 3) {
                    display = new Display();
                    display.setData(dataList);
                    display.setType("grid_simple");
                    displays.add(display);
                } else {
                    display = new Display();
                    display.setData(dataList);
                    display.setType("big");
                    displays.add(display);
                }
                lastState = currentState;
                dataList = new ArrayList<>();
            }

            if (count < length) {
                try {
                    dataList.add(Tool.readObject(response.getJSONObject(count)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            count++;
        }
        adapter.notifyDataSetChanged();
    }

    private int checkCount(int count) {
        int tmp = count % NUMBER_ITEM_TOTAL;
        if (tmp < NUMBER_ITEM_VIEW_1) return 1;
        if (tmp < NUMBER_ITEM_VIEW_2 + NUMBER_ITEM_VIEW_1) return 2;
        if (tmp < NUMBER_ITEM_VIEW_3 + NUMBER_ITEM_VIEW_2 + NUMBER_ITEM_VIEW_1) return 3;
        return 4;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        System.out.println("----------------------------onActivityResult: " + requestCode + " - resultCode: " + resultCode);

        switch (requestCode) {
            case Constants.STATUS_NETWORK_CONNECT:
                getData(false);
                break;
        }

    }

    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    //    @Override
    //    public void onRefresh() {
    //        swipeRefreshLayout.setRefreshing(true);
    //        p = 0;
    //        getData(true, false);
    //    }
}
