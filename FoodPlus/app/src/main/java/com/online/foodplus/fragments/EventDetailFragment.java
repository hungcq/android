package com.online.foodplus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.R;
import com.online.foodplus.adapters.ListRoundAdapter;
import com.online.foodplus.libraries.GridSpacingItemDecoration;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.models.Base;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by thanhthang on 14/02/2017.
 */

public class EventDetailFragment extends BaseFragment implements View.OnClickListener {
    private TextView tvTitle, tvDatetime, /*tvAppbarTitle,*/
            tvToolbarTitle, tvDescription;
    private ImageView imgFeature, imgToolbarBack, imgToolbarMenu;
    private Button bShare;
    private WebView webContent;
    private String id, title, content;
    private ArrayList<Base> datas;
    private RecyclerView rvRecent;
    private ListRoundAdapter adapter;
    private NestedScrollView scrollView;
    private ProgressBar pbToolbar;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        init(view);
        initData();
        listener();
        received();
        getData(false);
        return view;
    }

    private void init(View view) {
        imgToolbarBack = (ImageView) view.findViewById(R.id.imgToolbarBack);
        imgToolbarMenu = (ImageView) view.findViewById(R.id.imgToolbarMenu);
        pbToolbar = (ProgressBar) view.findViewById(R.id.pbToolbar);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);

        scrollView = (NestedScrollView) view.findViewById(R.id.scrollView);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvDatetime = (TextView) view.findViewById(R.id.tvDatetime);
        bShare = (Button) view.findViewById(R.id.bShare);
        webContent = (WebView) view.findViewById(R.id.webContent);
        imgFeature = (ImageView) view.findViewById(R.id.imgFeature);
        rvRecent = (RecyclerView) view.findViewById(R.id.rvRecent);
    }

    private void initData() {
        //TOOLBAR
        imgToolbarMenu.setVisibility(View.GONE);
        pbToolbar.setVisibility(View.INVISIBLE);
        tvToolbarTitle.setText("...");
        pbToolbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

        //RECENT
        datas = new ArrayList<>();
        adapter = new ListRoundAdapter(datas, true);
        rvRecent.setNestedScrollingEnabled(false);
        rvRecent.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecent.addItemDecoration(new GridSpacingItemDecoration(1, (int) getActivity().getResources().getDimension(R.dimen._3sdp), false));
        rvRecent.setAdapter(adapter);
        rvRecent.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                id = datas.get(position).getId();
                getData(true);
            }
        }));
        webContent.getSettings().setDefaultTextEncodingName("utf-8");
    }

    private void listener() {
        imgToolbarBack.setOnClickListener(this);
        bShare.setOnClickListener(this);
    }

    private void received() {
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey("id"))
            id = bundle.getString("id");
    }

    private void getData(boolean reset) {
        if (!isLoading && id != null) {
            isLoading = true;
            if (reset) {
                scrollView.smoothScrollTo(0, 0);
                imgFeature.setVisibility(View.GONE);
                tvToolbarTitle.setText(getResources().getString(R.string.su_kien_extended));
                tvTitle.setText("");
                tvDescription.setText("");
                webContent.loadData("", "text/html; charset=utf-8", "UTF-8");
                datas.clear();
            }

            RequestParams params = new RequestParams();
            params.put("opt", 1);
            params.put("id", id);
            if (getResources().getBoolean(R.bool.isTablet))
                params.put("scr", "600x600");
            else
                params.put("scr", "400x400");

            //            System.out.println("-----------------------REQUEST: " + getResources().getString(R.string.api_eventsrv) + "?" + params.toString());
            Tool.get(getResources().getString(R.string.api_eventsrv), params, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    pbToolbar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    isLoading = false;
                    pbToolbar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // System.out.println("-----------------------RESPONSE: " + response);

                    if (isAdded()) {

                        if (response.has("detail")) {
                            try {
                                JSONObject joDetail = response.getJSONObject("detail");
                                title = Tool.getStringJson("te", joDetail);
                                content = Tool.getStringJson("mota", joDetail);

                                JSONArray imagesArray = joDetail.getJSONArray("images");
                                if (imagesArray.length() > 0) {
                                    imgFeature.setVisibility(View.VISIBLE);
                                    Glide.with(imgFeature.getContext()).load(imagesArray.getString(0))
                                            .crossFade()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(imgFeature);
                                } else
                                    imgFeature.setVisibility(View.GONE);


                                String datetime = Tool.getStringJson("ngaytao", joDetail);
                                if (datetime != null)
                                    tvDatetime.setText(datetime);
                                if (datetime != null)
                                    tvDatetime.setText(datetime);
                                String description = Tool.getStringJson("tencuahang", joDetail);
                                if (description != null)
                                    tvDescription.setText(description);
                                if (datetime != null)
                                    tvDatetime.setText(datetime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (title != null) {
                                tvTitle.setText(title);
                                tvToolbarTitle.setText(title);
                            }

                            if (content != null && !content.equals("null"))
                                webContent.loadData("<style>img{max-width:100%;}</style>" + content.replaceAll("(style=\".*?\")", "").replace("\r\n", "<br/>"), "text/html; charset=utf-8", "UTF-8");

                        }
                        if (response.has("recent")) {
                            JSONArray jaRecent = null;
                            try {
                                jaRecent = response.getJSONArray("recent");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (jaRecent != null) {
                                int len = jaRecent.length();
                                if (len > 6)
                                    len = 6;
                                for (int i = 0; i < len; i++) {
                                    try {
                                        String id = Tool.getStringJson("id", jaRecent.getJSONObject(i));
                                        String title = Tool.getStringJson("te", jaRecent.getJSONObject(i));
                                        String description = Tool.getStringJson("mota", jaRecent.getJSONObject(i));
                                        String datetime = Tool.getStringJson("ngaytao", jaRecent.getJSONObject(i));
                                        Base base = new Base();
                                        base.setId(id);
                                        base.setTitle(title);
                                        base.setDescription(description);
                                        base.setDatetime(datetime);
                                        if (jaRecent.getJSONObject(i).has("images")) {
                                            JSONArray imagesArray = jaRecent.getJSONObject(i).getJSONArray("images");
                                            ArrayList<String> images = new ArrayList<>();
                                            if (imagesArray.length() > 0) {
                                                for (int j = 0; j < imagesArray.length(); j++)
                                                    images.add(imagesArray.getString(j));
                                            }
                                            base.setImages(images);
                                        }
                                        datas.add(base);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                scrollView.smoothScrollTo(0, 0);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if (isAdded())
                        ToastCustom.show(getActivity(), getResources().getString(R.string.please_retry_later), ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (isAdded())
                        ToastCustom.show(getActivity(), getResources().getString(R.string.please_retry_later), ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgToolbarBack:
                getActivity().onBackPressed();
                break;
            case R.id.bShare:
                if (title != null && content != null) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + ": " + content);
                    startActivity(Intent.createChooser(sharingIntent, "Chia sẻ"));
                }
                break;
        }
    }


}
