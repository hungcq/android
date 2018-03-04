package com.online.foodplus.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.adapters.FindAdapter;
import com.online.foodplus.dialogs.SelectCityDialog;
import com.online.foodplus.interfaces.BaseListener;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.libraries.SpacesItemDecoration;
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
 * ISSUES
 * 1. How to avoid multiple triggers on EditText while user is typing?
 * http://stackoverflow.com/questions/10217051/how-to-avoid-multiple-triggers-on-edittext-while-user-is-typing
 * 2. Animation Recycleview
 * https://github.com/wasabeef/recyclerview-animators
 */

public class FindActivity extends BaseActivity implements View.OnClickListener, BaseListener {
    private CoordinatorLayout layoutContainer;

    private EditText etFind;
    private TextView tvCity, tvNote;
    private String city_id;
    private SharedPreferences pref;
    private ImageView imgConfig;
    private RecyclerView rvFind;
    private FindAdapter adapter;
    private ArrayList<Base> datas;

    private boolean isSending = false;
    private ProgressBar progressBar;

    //Issue 1
    private final int TRIGGER_SERACH = 1;
    private final long SEARCH_TRIGGER_DELAY_IN_MS = 500;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TRIGGER_SERACH) {
                triggerSearch();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        init();
        initNetworkConnectedCheck(layoutContainer);
        listener();
        initData();
        loadData(null);
    }


    private void init() {
        layoutContainer = (CoordinatorLayout) findViewById(R.id.layoutContainer);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvNote = (TextView) findViewById(R.id.tvNote);
        tvCity = (TextView) findViewById(R.id.tvCity);
        etFind = (EditText) findViewById(R.id.etFind);
        rvFind = (RecyclerView) findViewById(R.id.rvFind);
        imgConfig = (ImageView) findViewById(R.id.imgFilter);
    }

    private void listener() {
        tvCity.setOnClickListener(this);
        imgConfig.setOnClickListener(this);
        etFind.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.removeMessages(TRIGGER_SERACH);
                handler.sendEmptyMessageDelayed(TRIGGER_SERACH, SEARCH_TRIGGER_DELAY_IN_MS);
            }
        });
        etFind.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                }
                return false;
            }
        });
        rvFind.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("id", datas.get(position).getId());
                intent.putExtra("t", datas.get(position).getTid());
                intent.putExtra("cid", datas.get(position).getCid());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }));
    }

    private void initData() {
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

        //Đọc dữ liệu Tỉnh thành đã lưu trong SharePreference
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        city_id = pref.getString("city_id", "");
        String city_title = pref.getString("city_title", getResources().getString(R.string.all));
        tvCity.setText(city_title);

        //Thiết lập RecycleView
        datas = new ArrayList<>();
        adapter = new FindAdapter(datas);
        rvFind.setLayoutManager(new LinearLayoutManager(this));
        rvFind.addItemDecoration(new SpacesItemDecoration(1));
        //Animation
        rvFind.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        rvFind.setAdapter(scaleAdapter);
    }

    private void loadData(String term) {
        if (!Tool.isNetworkConnected(getApplicationContext())) {
            showNetworkStatusNotification(true);
            return;
        }
        if (!isSending) {
            isSending = true;
            progressBar.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.put("p", 1);
            String cityID = pref.getString("city_id", "");
            if (!cityID.equals(""))
                params.put("city", cityID);
            params.put("scr", "200x200");
            if (term != null && !term.equals(""))
                params.put("key", term);

            //System.out.println("------------------------REQUEST: " + getResources().getString(R.string.api_search) + "?" + params.toString());

            Tool.get(getResources().getString(R.string.api_search), params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // System.out.println("----------------------RESPONSE: "+response);
                            datas.clear();
                            int length = response.length();
                            if (length > 0) {
                                tvNote.setVisibility(View.GONE);
                                rvFind.setVisibility(View.VISIBLE);
                                for (int i = 0; i < length; i++) {
                                    try {
                                        datas.add(Tool.readObject(response.getJSONObject(i)));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter.notifyDataChanged();
                            } else {
                                tvNote.setVisibility(View.VISIBLE);
                                rvFind.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onFinish() {
                            isSending = false;
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

            );
        }

    }

    private void triggerSearch() {
        String term = etFind.getText().toString();

        if (term.length() == 0)
            loadData(null);
        else if (term.length() >= 2)
            loadData(term);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCity:
                city_id = pref.getString("city_id", "");
                SelectCityDialog newFragment = SelectCityDialog.newInstance(city_id);
                newFragment.show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.imgFilter:
                startActivity(new Intent(getApplicationContext(), SearchAdvancedActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void updateResult(Base base) {
        tvCity.setText(base.getTitle());
        pref.edit().putString("city_id", base.getId()).commit();
        pref.edit().putString("city_title", base.getTitle()).commit();
        city_id = base.getId();
        triggerSearch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        System.out.println("----------------------------onActivityResult: " + requestCode + " - resultCode: " + resultCode);

        switch (requestCode) {
            case Constants.STATUS_NETWORK_CONNECT:
                loadData(null);
                break;
        }

    }
}
