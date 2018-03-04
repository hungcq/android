package com.online.foodplus.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.adapters.CategoryAdapter;
import com.online.foodplus.adapters.ReviewAdapter;
import com.online.foodplus.dialogs.CommentDialog;
import com.online.foodplus.dialogs.ReviewDialog;
import com.online.foodplus.dialogs.ReviewListDialog;
import com.online.foodplus.interfaces.BaseListener;
import com.online.foodplus.libraries.GridSpacingItemDecoration;
import com.online.foodplus.models.Base;
import com.online.foodplus.models.Display;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.DetailView;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class DetailActivity extends BaseActivity implements View.OnClickListener, BaseListener {
    private CoordinatorLayout layoutContainer;
    private ProgressDialog progress;
    //    private ArrayList<Display> contents;
    private String id, cid, t, title, description, rating, content, feature, isLike = "0", isPin = "0";
    private int p = 1;
    private LinearLayout lbShare, lbPin, lbFavourite,/* lbCheckIn,*/
            lbComment;
    //private ImageView backImage, menuImage;
    private ImageView imgFavourite, imgPin;
    private ProgressBar pbToolbar;
    private DetailView detailView;
    //Review
    private TextView tvReview, tvNote;
    private RecyclerView rvReview;
    private ReviewAdapter adapterReview;
    private ArrayList<Base> reviews;
    private Button bMoreReview;
    private boolean isSending = false;
    private boolean isPinChanged = false;


    private CategoryAdapter adapter;
    private RecyclerView rvDetail;
    private LinearLayoutManager layoutManager;
    private ArrayList<Display> datas;

    //Review
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        received();
        initToolbarSimple("", true);
        init();
        initNetworkConnectedCheck(layoutContainer);
        initData();
        init_review();
        listener();
        setUpDrawerLayout(0);
        getData();
    }


    private void received() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = (getIntent().hasExtra("id") && extras.getString("id") != null) ? extras.getString("id") : "1";
            cid = (getIntent().hasExtra("cid") && extras.getString("cid") != null) ? extras.getString("cid") : "1";
            t = (getIntent().hasExtra("t") && extras.getString("t") != null) ? extras.getString("t") : "1";
        }
    }

    private void init() {
        layoutContainer = (CoordinatorLayout) findViewById(R.id.layoutContainer);

        //Toolbar
        pbToolbar = (ProgressBar) findViewById(R.id.pbToolbar);

        //Nav Header
        lbShare = (LinearLayout) findViewById(R.id.lbShare);
        lbPin = (LinearLayout) findViewById(R.id.lbPin);
        lbFavourite = (LinearLayout) findViewById(R.id.lbFavourite);
        lbComment = (LinearLayout) findViewById(R.id.lbComment);

        imgFavourite = (ImageView) findViewById(R.id.imgFavourite);
        imgPin = (ImageView) findViewById(R.id.imgPin);

        //Detail
        detailView = (DetailView) findViewById(R.id.detailView);
        rvDetail = (RecyclerView) findViewById(R.id.rvDetail);

        //Review
        tvReview = (TextView) findViewById(R.id.tvReview);
        tvNote = (TextView) findViewById(R.id.tvNote);
        rvReview = (RecyclerView) findViewById(R.id.rvReview);
        bMoreReview = (Button) findViewById(R.id.bMoreReview);
    }

    private void initData() {
        pbToolbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

        //Product Relate Loop
        datas = new ArrayList<>();
        adapter = new CategoryAdapter(this, datas);
        layoutManager = new LinearLayoutManager(this);
        rvDetail.setLayoutManager(layoutManager);
        rvDetail.addItemDecoration(new GridSpacingItemDecoration(1, (int) getResources().getDimension(R.dimen._2sdp), false));

        //Animation
        rvDetail.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        rvDetail.setAdapter(scaleAdapter);
    }

    private void init_review() {
        reviews = new ArrayList<>();
        adapterReview = new ReviewAdapter(reviews);
        rvReview.setLayoutManager(new GridLayoutManager(this, 2));
        rvReview.setAdapter(adapterReview);
        rvReview.addItemDecoration(new GridSpacingItemDecoration(2, (int) getResources().getDimension(R.dimen._3sdp), false));
    }

    private void listener() {

        //Nav Header
        lbShare.setOnClickListener(this);
        lbPin.setOnClickListener(this);
        lbFavourite.setOnClickListener(this);
        lbComment.setOnClickListener(this);

        //Review
        tvReview.setOnClickListener(this);
        tvNote.setOnClickListener(this);
        bMoreReview.setOnClickListener(this);
    }

    private void getData() {
        if (!Tool.isNetworkConnected(getApplicationContext())) {
            showNetworkStatusNotification(true);
            return;
        }

        if (!isSending) {
            isSending = true;
            RequestParams params = new RequestParams();
            params.put("id", id);
            params.put("cid", cid);
            params.put("t", t);
            params.put("p", p);
            params.put("scr", "400x400");
            System.out.println("------------------------------DETAIL REQUEST: " + getResources().getString(R.string.api_detail) + "?" + params.toString());

            Tool.post(getResources().getString(R.string.api_detail), params, new JsonHttpResponseHandler() {


                @Override
                public void onStart() {
                    pbToolbar.setVisibility(View.VISIBLE);

                    progress = new ProgressDialog(DetailActivity.this);
                    progress.setIndeterminate(true);
                    progress.show();
                    progress.setContentView(R.layout.cusom_dialog_progress);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //System.out.println("------------------------------DETAIL RESPONSE: " + response);
                    try {
                        if (response.has("content")) {
                            JSONArray jsonArray = response.getJSONArray("content");
                            for (int k = 0, len = jsonArray.length(); k < len; k++) {
                                Display display = Tool.readDisplay(jsonArray.getJSONObject(k));
                                if (display != null)
                                    datas.add(display);
                            }

                        }
                        if (response.has("detail")) {
                            JSONObject object = response.getJSONObject("detail").getJSONObject("object");
                            Base base = Tool.readObject(object);

                            TextView tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
                            if (base != null) {
                                detailView.setData(base);
                                if (base.getTitle() != null)
                                    tvToolbarTitle.setText(base.getTitle());
                                title = base.getTitle();
                                content = base.getContent();
                                description = base.getDescription();
                                rating = base.getRating();

                                if (base.getImages() != null && base.getImages().size() > 0)
                                    feature = base.getImages().get(0);
                            }
                            isLike = Tool.getStringJson("is_like", object);
                            isPin = Tool.getStringJson("is_spin", object);
                            if (isLike != null && isLike.equals("1"))
                                imgFavourite.setImageResource(R.drawable.ic_heart_pink);

                            if (isPin != null && isPin.equals("1"))
                                imgPin.setImageResource(R.drawable.ic_pin_selected);

                            if (object.has("rate"))
                                detailView.setRate(Tool.getIntJson("star1", object.getJSONObject("rate")), Tool.getIntJson("star2", object.getJSONObject("rate")), Tool.getIntJson("star3", object.getJSONObject("rate")), Tool.getIntJson("star4", object.getJSONObject("rate")), Tool.getIntJson("star5", object.getJSONObject("rate")));
                        }

                        if (response.has("review")) {
                            JSONArray jsonArray = response.getJSONArray("review");
                            int length = jsonArray.length();
                            Base base;
                            if (length > 0) {
                                tvNote.setVisibility(View.GONE);
                                tvReview.setVisibility(View.VISIBLE);
                                rvReview.setVisibility(View.VISIBLE);
                                bMoreReview.setVisibility(View.VISIBLE);
                                if (length > 6)
                                    length = 6;
                                for (int i = 0; i < length; i++) {
                                    base = new Base();
                                    base.setTitle(Tool.getStringJson("title", jsonArray.getJSONObject(i)));
                                    base.setContent(Tool.getStringJson("content", jsonArray.getJSONObject(i)));
                                    base.setRating(Tool.getStringJson("rating", jsonArray.getJSONObject(i)));
                                    base.setDescription(Tool.getStringJson("username", jsonArray.getJSONObject(i)));
                                    base.setFeature(Tool.getStringJson("avatar", jsonArray.getJSONObject(i)));
                                    reviews.add(base);
                                }
                                adapterReview.notifyDataSetChanged();
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Display footer = new Display();
                    footer.setType("footer");
                    datas.add(footer);
                    //UPDATE VIEW
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + " (error " + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
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
                    pbToolbar.setVisibility(View.GONE);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        String user_id = Tool.getUserId(this);

        switch (v.getId()) {
            case R.id.lbPin:
                if (Tool.isLogin(getApplicationContext())) {
                    if (!isSending) {
                        isSending = true;
                        final String newPin = isPin.equals("1") ? "0" : "1";
                        RequestParams params = new RequestParams();
                        params.put("g", newPin);
                        params.put("uid", Tool.getUserId(getApplicationContext()));
                        params.put("opt", 4);
                        params.put("cid", cid);
                        params.put("t", t);
                        params.put("id", id);
                        // System.out.println("-------------------------- PIN REQUEST: " + getResources().getString(R.string.api_votesrv) + "?" + params.toString());
                        Tool.post(getResources().getString(R.string.api_votesrv), params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                int status = Tool.getIntJson("status", response);
                                if (status == 1) {
                                    isPin = newPin;
                                    imgPin.setImageResource(isPin.equals("1") ? R.drawable.ic_pin_selected : R.drawable.ic_pin_white);
                                    isPinChanged = true;
                                }
                            }

                            @Override
                            public void onFinish() {
                                isSending = false;
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + " (error" + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + " (error" + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                            }
                        });
                    }
                } else {
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), Constants.STATUS_CODE_LOGIN);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;
            case R.id.lbFavourite:
                if (Tool.isLogin(getApplicationContext())) {
                    if (!isSending) {
                        isSending = true;
                        final String newLike = isLike.equals("1") ? "0" : "1";
                        RequestParams params = new RequestParams();
                        params.put("s", newLike);
                        params.put("uid", Tool.getUserId(getApplicationContext()));
                        params.put("opt", 1);
                        params.put("cid", cid);
                        params.put("t", t);
                        params.put("id", id);
                        // System.out.println("-------------------------- LIKE REQUEST: " + params.toString());
                        Tool.post(getResources().getString(R.string.api_votesrv), params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                int status = Tool.getIntJson("status", response);
                                if (status == 1) {
                                    isLike = newLike;
                                    imgFavourite.setImageResource(isLike.equals("1") ? R.drawable.ic_heart_pink : R.drawable.ic_heart_white);
                                }
                            }

                            @Override
                            public void onFinish() {
                                isSending = false;
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + " (error" + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + " (error" + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                            }
                        });
                    }
                } else {
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), Constants.STATUS_CODE_LOGIN);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;
            case R.id.lbShare:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + ": " + content);
                startActivity(Intent.createChooser(sharingIntent, "Chia sẻ"));
                break;
            case R.id.lbComment:
                if (Tool.isLogin(getApplicationContext())) {
                    DialogFragment newFragment = CommentDialog.newInstance(title, description, feature, id, t, cid, user_id);
                    newFragment.show(fm, "dialogComment");
                } else {
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), Constants.STATUS_CODE_LOGIN);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;

            //REVIEW
            case R.id.tvReview:
            case R.id.tvNote:
                if (!user_id.equals("")) {
                    DialogFragment newFragment = ReviewDialog.newInstance(title, description, feature, id, t, cid, user_id);
                    newFragment.show(fm, "dialogReview");
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                break;
            case R.id.bMoreReview:
                DialogFragment newFragment = ReviewListDialog.newInstance(title, id, t, cid);
                newFragment.show(fm, "dialogReviewList");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        System.out.println("----------------------------onActivityResult: " + requestCode + " - resultCode: " + resultCode);

        switch (requestCode) {
            case Constants.STATUS_CODE_DIALOG_COMMENT:
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialogComment");
                if (fragment != null)
                    fragment.onActivityResult(requestCode, resultCode, data);
                break;
            case Constants.STATUS_NETWORK_CONNECT:
                getData();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (isPinChanged) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);//return STATUS_CODE_PIN_CHANGE (930) code for PinActivity
        }
        super.onBackPressed();
    }

    @Override
    public void updateResult(Base base) {
        datas.clear();
        reviews.clear();
        getData();
        adapterReview.notifyDataSetChanged();
    }
}