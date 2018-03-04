package com.online.foodplus.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ogaclejapan.arclayout.ArcLayout;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.adapters.CategoryAdapter;
import com.online.foodplus.libraries.GridSpacingItemDecoration;
import com.online.foodplus.models.Content;
import com.online.foodplus.models.Display;
import com.online.foodplus.models.ImageInfo;
import com.online.foodplus.models.MySliderView;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * Created by thanhthang on 14/12/2016.
 */

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout layoutContainer;

    private ArcLayout layoutArc;
    private ArrayList<ImageInfo> imageInfos;
    private SliderLayout sliderLayout;
    private ImageView sliderBackground;

    // Toolbar
    private ImageView imgMenu, imgSearch;
    private ProgressDialog progress;

    private LinearLayout layoutNavHeaderFixed;

    private CategoryAdapter adapter;
    private RecyclerView rvMain;
    private ArrayList<Display> datas;
    private boolean isSending = false;
    private boolean isSliderLoaded = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //RunLib.setup(this, "1186", "IDTV_" + getApplicationInfo().loadLabel(getPackageManager()).toString(), "GooglePlay", "pu");
        init();
        initNetworkConnectedCheck(layoutContainer);
        listener();
        initData();
        getData();
        initFooter(this);
        bHomeFooter.setSelected(true);
        setUpDrawerLayout(R.id.nav_home_item);
    }

    private void listener() {
        //Toolbar
        imgMenu.setOnClickListener(this);
        imgSearch.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);
    }


    private void init() {
        //Toolbar
        imgMenu = (ImageView) findViewById(R.id.img_menu_toolbar);
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        sliderBackground = (ImageView) findViewById(R.id.slider_background);

        //Content
        sliderLayout = (SliderLayout) findViewById(R.id.slider_layout);
        layoutArc = (ArcLayout) findViewById(R.id.layout_arc);

        //Slider
        initSlider();

        layoutContainer = (CoordinatorLayout) findViewById(R.id.layoutContainer);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layoutNavHeaderFixed = (LinearLayout) findViewById(R.id.layoutNavHeaderFixed);
        rvMain = (RecyclerView) findViewById(R.id.rvMain);
    }

    private void initData() {
        datas = new ArrayList<>();
        adapter = new CategoryAdapter(this, datas);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        //        rvMain.setAdapter(adapter);
        rvMain.addItemDecoration(new GridSpacingItemDecoration(1, (int) getResources().getDimension(R.dimen._2sdp), false));

        //Animation
        rvMain.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        rvMain.setAdapter(scaleAdapter);

        //Refresh
        TypedValue typed_value = new TypedValue();
        getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typed_value, true);
        swipeRefreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.Red), ContextCompat.getColor(getApplicationContext(), R.color.Green), ContextCompat.getColor(getApplicationContext(), R.color.Yellow), ContextCompat.getColor(getApplicationContext(), R.color.Blue));

    }

    private void getData() {
        getData(false);
    }

    private void getData(final boolean isRefresh) {
        if (!Tool.isNetworkConnected(getApplicationContext())) {
            showNetworkStatusNotification(true);
            return;
        }
        if (!isSending) {
            isSending = true;
            RequestParams params = new RequestParams();
            params.put("scr", "400x400");
            Tool.get(getResources().getString(R.string.api_main), params, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    progress = new ProgressDialog(MainActivity.this);
                    progress.setIndeterminate(true);
                    progress.show();
                    progress.setContentView(R.layout.cusom_dialog_progress);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //System.out.println("------------RESPONSE: " + response);
                    datas.clear();
                    try {
                        //SLIDER
                        if (!isSliderLoaded) {
                            JSONArray sliderJsonArray = response.getJSONObject("slider").getJSONArray("images");
                            ArrayList<String> sliderImages = new ArrayList<>();
                            for (int x = 0; x < sliderJsonArray.length(); x++)
                                sliderImages.add(sliderJsonArray.getString(x));
                            if (sliderImages.size() > 0)
                                Glide.with(getApplicationContext()).load(sliderImages.get(0))
                                        .centerCrop()
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(sliderBackground);
                            //                                Picasso.with(getApplicationContext()).load(sliderImages.get(0)).resize(600, 300).centerCrop().into(sliderBackground);
                            isSliderLoaded = true;
                        }

                        //CONTENT
                        JSONArray jsonArray = response.getJSONArray("content");
                        int length = jsonArray.length();
                        Content content;
                        Display display, footer;
                        for (int i = 0; i < length; i++) {
                            content = new Content();
                            final String title = Tool.getStringJson("title", jsonArray.getJSONObject(i));
                            String icon = Tool.getStringJson("icon", jsonArray.getJSONObject(i));
                            final String cid = Tool.getStringJson("cid", jsonArray.getJSONObject(i));
                            final String tid = Tool.getStringJson("tid", jsonArray.getJSONObject(i));
                            content.setTitle(title);
                            content.setIcon(icon);
                            content.setTid(tid);
                            content.setCid(cid);
                            display = new Display();
                            display.setType("titlebox");
                            display.setTitle(title);
                            display.setIcon(icon);
                            display.setTid(tid);
                            display.setCid(cid);
                            datas.add(display);
                            if (jsonArray.getJSONObject(i).has("data")) {
                                JSONArray jsonData = jsonArray.getJSONObject(i).getJSONArray("data");
                                int len = jsonData.length();
                                for (int j = 0; j < len; j++)
                                    datas.add(Tool.readDisplay(jsonData.getJSONObject(j)));
                            }

                            //mains.add(content);


                            if (!isRefresh) {
                                // NAV HEADER FIXED AND NAV SLIDER
                                ImageView imgNavFixed, imgNavArc;
                                LinearLayout linearLayout;

                                // IMAGE FOR NAV HEADER FIXED
                                linearLayout = new LinearLayout(getApplicationContext());
                                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                                linearLayout.setGravity(Gravity.CENTER);

                                //INTEN
                                final Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("title", title);
                                //bundle.putString("active", "monan");
                                bundle.putString("t", tid);
                                bundle.putString("cid", cid);
                                intent.putExtras(bundle);


                                imgNavFixed = new ImageView(new android.view.ContextThemeWrapper(MainActivity.this, R.style.SelectableBackground), null, 0);
                                imgNavFixed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // scrollTo(mains.get(temp).getTitle());
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                });
                                Picasso.with(getApplicationContext())
                                        .load(icon)
                                        .resize((int) getResources().getDimension(R.dimen._35sdp), (int) getResources().getDimension(R.dimen._35sdp))
                                        .centerCrop()
                                        .into(imgNavFixed);
                                linearLayout.addView(imgNavFixed);
                                layoutNavHeaderFixed.addView(linearLayout);

                                // IMAGE FOR NAV SLIDER
                                imgNavArc = new ImageView(new android.view.ContextThemeWrapper(MainActivity.this, R.style.SelectableBackground), null, 0);
                                imgNavArc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // scrollTo(mains.get(temp).getTitle());
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                });
                                Picasso.with(getApplicationContext())
                                        .load(icon)
                                        .resize((int) getResources().getDimension(R.dimen._35sdp), (int) getResources().getDimension(R.dimen._35sdp))
                                        .centerCrop()
                                        .into(imgNavArc);
                                layoutArc.addView(imgNavArc);

                            }
                        }

                        footer = new Display();
                        footer.setType("footer");
                        datas.add(footer);

                        adapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + " (error " + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onFinish() {
                    isSending = false;
                    if (progress != null && progress.isShowing())
                        progress.dismiss();
                    if (swipeRefreshLayout.isShown())
                        swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            if (swipeRefreshLayout.isShown())
                swipeRefreshLayout.setRefreshing(false);
        }

    }

    private void initSlider() {
        imageInfos = new ArrayList<>();
        imageInfos.add(new ImageInfo(R.drawable.icon_food_white, getResources().getString(R.string.thuc_pham)));
        imageInfos.add(new ImageInfo(R.drawable.icon_recipe_white, getResources().getString(R.string.nau_an)));
        imageInfos.add(new ImageInfo(R.drawable.icon_knife_white, getResources().getString(R.string.do_an)));
        imageInfos.add(new ImageInfo(R.drawable.icon_coffee_white, getResources().getString(R.string.do_uong)));
        imageInfos.add(new ImageInfo(R.drawable.icon_spa_white, getResources().getString(R.string.khoe_dep)));

        if (sliderLayout != null)
            for (ImageInfo imageInfo : imageInfos) {
                MySliderView mySliderView = new MySliderView(getApplicationContext(), imageInfo);
                mySliderView.image(imageInfo.getImage()).setScaleType(BaseSliderView.ScaleType.CenterInside);
                sliderLayout.addSlider(mySliderView);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        System.out.println("----------------------------onActivityResult: " + requestCode + " - resultCode: " + resultCode);

        switch (requestCode) {
            case Constants.STATUS_NETWORK_CONNECT:
                getData();
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgSearch:
                startActivity(new Intent(getApplicationContext(), FindActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.img_menu_toolbar:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
        }
    }

    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getData(true);
    }


}
