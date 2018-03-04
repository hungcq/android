package com.online.foodplus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.R;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by thanhthang on 24/02/2017.
 */

public class ShopActivity extends BaseActivity implements View.OnClickListener {
    private CoordinatorLayout layoutContainer;
    private CollapsingToolbarLayout collapsing_toolbar;
    private AppBarLayout appbar;
    private Button bShare;
    private TextView tvTitle, tvDescription, tvOpenTime, tvCity, tvToolbarTitle, tvPhone;
    private String id, cid, t, titleText = "", textDescription;
    private ImageView imgFeature, imgToolbarBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        init();
        initNetworkConnectedCheck(layoutContainer);
        received();
        listener();
        getData();
    }

    private void init() {
        layoutContainer = (CoordinatorLayout) findViewById(R.id.layoutContainer);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        bShare = (Button) findViewById(R.id.bShare);
        imgToolbarBack = (ImageView) findViewById(R.id.imgToolbarBack);
        imgFeature = (ImageView) findViewById(R.id.imgFeature);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvOpenTime = (TextView) findViewById(R.id.tvOpenTime);
        tvCity = (TextView) findViewById(R.id.tvCity);
    }

    private void received() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = (getIntent().hasExtra("id") && extras.getString("id") != null) ? extras.getString("id") : "99";
            cid = (getIntent().hasExtra("cid") && extras.getString("cid") != null) ? extras.getString("cid") : "99";
            t = (getIntent().hasExtra("t") && extras.getString("t") != null) ? extras.getString("t") : "1";
        }
    }

    private void listener() {
        imgToolbarBack.setOnClickListener(this);
        bShare.setOnClickListener(this);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (collapsing_toolbar.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsing_toolbar)) {
                    // System.out.println("-----------------------Collapsed");
                    if (titleText != null)
                        tvToolbarTitle.setText(titleText);
                    tvToolbarTitle.animate().alpha(1).setDuration(600);
                } else {
                    // System.out.println("-----------------------Expanded");
                    tvToolbarTitle.setText("");
                    tvToolbarTitle.animate().alpha(0).setDuration(600);
                }
            }
        });
    }

    private void getData() {
        if (!Tool.isNetworkConnected(getApplicationContext())) {
            showNetworkStatusNotification(true);
            return;
        }

        if (id != null && cid != null && t != null) {
            RequestParams params = new RequestParams();
            params.put("id", id);
            params.put("cid", cid);
            params.put("t", t);
            if (getResources().getBoolean(R.bool.isTablet))
                params.put("scr", "600x600");
            else
                params.put("scr", "400x400");
            Tool.post(getResources().getString(R.string.api_detail), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response.has("detail")) {
                        try {
                            JSONObject object = response.getJSONObject("detail").getJSONObject("object");
                            String title = Tool.getStringJson("title", object);
                            String description = Tool.getStringJson("description", object);
                            String open = Tool.getStringJson("open", object);
                            String phone = Tool.getStringJson("phone", object);
                            String tentp = Tool.getStringJson("tentp", object);
                            String feature = null;
                            if (object.has("images") && object.getJSONArray("images").length() > 0)
                                feature = object.getJSONArray("images").getString(0);
                            if (title != null) {
                                tvTitle.setText(title);
                                titleText = title;
                            }

                            if (description != null) {
                                textDescription = description;
                                tvDescription.setText(description);
                            }
                            if (phone != null)
                                tvPhone.setText(phone);
                            if (open != null)
                                tvOpenTime.setText(open);
                            if (tentp != null)
                                tvCity.setText(tentp);
                            if (feature != null)
                                Glide.with(getApplicationContext()).load(feature)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imgFeature);
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + " (error " + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + " (error " + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgToolbarBack:
                onBackPressed();
                break;
            case R.id.bShare:
                if (titleText != null && textDescription != null) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, titleText);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, titleText + ": " + textDescription);
                    startActivity(Intent.createChooser(sharingIntent, "Chia sẻ"));
                }
                break;
        }
    }
}
