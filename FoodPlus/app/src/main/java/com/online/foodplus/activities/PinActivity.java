package com.online.foodplus.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.adapters.EventAdapter;
import com.online.foodplus.libraries.GridSpacingItemDecoration;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.models.Base;
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

public class PinActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private RecyclerView rvPin;
    private EventAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Base> datas;
    private LinearLayout layoutNote;
    //Request Connect
    private boolean isMoreDataAvailable = true;
    private int p = 0;
    private boolean isSending = false;
    private Paint paint = new Paint();
    private Button bDontRemind, bClose;
    private SharedPreferences pref;
    private boolean enableNote = true;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        initToolbarSimple("Danh sách đã lưu");
        init();
        listener();
        initData();
        setUpDrawerLayout(0);
        getData();
    }

    private void listener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        bClose.setOnClickListener(this);
        bDontRemind.setOnClickListener(this);
    }


    private void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        layoutNote = (LinearLayout) findViewById(R.id.layoutNote);
        rvPin = (RecyclerView) findViewById(R.id.rvPin);
        bClose = (Button) findViewById(R.id.bClose);
        bDontRemind = (Button) findViewById(R.id.bDontRemind);
    }

    private void initData() {
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        enableNote = pref.getBoolean("show_tut_pin", true);
        if (enableNote)
            layoutNote.setVisibility(View.VISIBLE);

        datas = new ArrayList<>();
        adapter = new EventAdapter(datas);
        layoutManager = new LinearLayoutManager(this);
        rvPin.setLayoutManager(layoutManager);
        rvPin.addItemDecoration(new GridSpacingItemDecoration(1, (int) getResources().getDimension(R.dimen._2sdp), false));
        //Animation
        rvPin.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        rvPin.setAdapter(scaleAdapter);
        rvPin.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(PinActivity.this, DetailActivity.class);
                intent.putExtra("id", datas.get(position).getId());
                intent.putExtra("t", datas.get(position).getTid());
                intent.putExtra("cid", datas.get(position).getCid());
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                startActivityForResult(intent, Constants.STATUS_CODE_PIN_CHANGE);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        //paint.setColor(Color.parseColor("#388E3C"));
                        paint.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_trash_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, paint);
                    } else {
                        paint.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_trash_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, paint);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int adapterPosition = viewHolder.getAdapterPosition();
                //                System.out.println("-------------------------onSwiped: " + adapterPosition);
                if (!isSending) {
                    isSending = true;
                    String id = datas.get(adapterPosition).getId();
                    String t = datas.get(adapterPosition).getTid();
                    String cid = datas.get(adapterPosition).getCid();
                    RequestParams params = new RequestParams();
                    params.put("g", 0);
                    params.put("uid", Tool.getUserId(getApplicationContext()));
                    params.put("opt", 4);
                    params.put("cid", cid);
                    params.put("t", t);
                    params.put("id", id);
                    //System.out.println("-------------------------- PIN REQUEST: " + getResources().getString(R.string.api_votesrv) + "?" + params.toString());
                    Tool.post(getResources().getString(R.string.api_votesrv), params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            //System.out.println("--------------------------PIN RESPONSE: " + response);
                            int status = Tool.getIntJson("status", response);
                            if (status == 1) {
                                //System.out.println("-------------------------PIN ADAPTER REMOVE AT : " + adapterPosition + " - TOTAL: " + adapter.getItemCount());
                                adapter.removeItem(adapterPosition);
                                if (adapter.getItemCount() == 0) {
                                    layoutNote.setVisibility(View.VISIBLE);
                                }
                            } else {
                                ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + "  (error " + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFinish() {
                            isSending = false;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + "  (error " + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later) + "  (error " + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                        }
                    });
                }

            }
        });
        itemTouchHelper.attachToRecyclerView(rvPin);
        rvPin.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typed_value, true);
        swipeRefreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.Red), ContextCompat.getColor(getApplicationContext(), R.color.Green), ContextCompat.getColor(getApplicationContext(), R.color.Yellow), ContextCompat.getColor(getApplicationContext(), R.color.Blue));

    }

    private void getData() {
        getData(false);
    }

    private void getData(final boolean isRefresh) {

        //Nếu Server còn dữ liệu hoặc có lệnh Refresh
        if (!isSending && (isMoreDataAvailable || isRefresh)) {
            isSending = true;
            p++;
            RequestParams params = new RequestParams();
            params.put("uid", Tool.getUserId(getApplicationContext()));
            params.put("p", p);
            params.put("scr", "200x200");
            //System.out.println("-----------------PIN REQUEST: " + getResources().getString(R.string.api_pinsrv) + "?" + params.toString());
            Tool.post(getResources().getString(R.string.api_pinsrv), params, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later), ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    ToastCustom.show(getApplicationContext(), getResources().getString(R.string.please_retry_later), ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    //System.out.println("-----------------------------PIN RESPONSE: " + response);
                    try {
                        if (isRefresh)
                            datas.clear();

                        int length = response.length();
                        if (length > 0) {

                            for (int i = 0; i < length; i++) {
                                Base base = Tool.readObject(response.getJSONObject(i));
                                datas.add(base);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            isMoreDataAvailable = false;      //Dừng request nếu server hết dữ liệu, trả về mảng trống []
                            if (p == 1)
                                layoutNote.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    isSending = false;
                    if (swipeRefreshLayout.isShown())
                        swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            if (swipeRefreshLayout.isShown())
                swipeRefreshLayout.setRefreshing(false);
        }
    }

    //
    //    @Override
    //    public void onBackPressed() {
    //        navigateUp();
    //    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        p = 0;
        getData(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //        System.out.println("----------------------------onActivityResult: " + requestCode + " - resultCode: " + resultCode);
        switch (requestCode) {
            case Constants.STATUS_CODE_PIN_CHANGE:   //Login Success
                if (resultCode == Activity.RESULT_OK) {
                    onRefresh();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDontRemind:
                pref.edit().putBoolean("show_tut_pin", false).apply();
                layoutNote.setVisibility(View.GONE);
                break;
            case R.id.bClose:
                layoutNote.setVisibility(View.GONE);
                break;
        }
    }
}
