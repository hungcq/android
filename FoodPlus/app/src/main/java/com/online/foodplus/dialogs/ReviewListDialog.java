package com.online.foodplus.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.R;
import com.online.foodplus.adapters.ReviewListAdapter;
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
 * Created by thanhthang on 25/02/2017.
 */

public class ReviewListDialog extends DialogFragment implements View.OnClickListener {
    private RecyclerView rvReview;
    private ReviewListAdapter adapter;
    private ArrayList<Base> datas;
    private boolean isSending = false, isMoreDataAvailable = true;
    private int p = 0;
    private static String cid, t, id, title;
    private LinearLayoutManager layoutManager;
    private ImageView imgToolbarBack;
    private TextView tvToolbarTitle;

    public static ReviewListDialog newInstance(String inputTitle, String inputId, String inputT, String inputCid) {
        title = inputTitle;
        cid = inputCid;
        t = inputT;
        id = inputId;
        ReviewListDialog mDialog = new ReviewListDialog();
        mDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullscreen);
        return mDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_review_list, container);
        init(view);
        initData();
        listener();
        getData();
        return view;
    }

    private void init(View view) {
        rvReview = (RecyclerView) view.findViewById(R.id.rvReview);
        imgToolbarBack = (ImageView) view.findViewById(R.id.imgToolbarBack);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
    }

    private void initData() {
        if (title != null)
            tvToolbarTitle.setText(title);
        datas = new ArrayList<>();
        adapter = new ReviewListAdapter(datas);
        layoutManager = new LinearLayoutManager(getActivity());
        rvReview.setLayoutManager(layoutManager);
        //        rvReview.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen._7sdp)));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation());
        rvReview.addItemDecoration(dividerItemDecoration);
        //Animation
        rvReview.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        rvReview.setAdapter(scaleAdapter);
    }

    private void listener() {
        imgToolbarBack.setOnClickListener(this);
        rvReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }

    private void getData() {
        if (!isSending && isMoreDataAvailable) {
            isSending = true;
            p++;
            RequestParams params = new RequestParams();
            params.put("cid", cid);
            params.put("opt", 1);
            params.put("p", p);
            params.put("id", id);
            params.put("scr", "200x200");
            Tool.get(getResources().getString(R.string.api_comsrv), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (isAdded() && response.has("review")) {
                        try {
                            JSONArray array = response.getJSONArray("review");
                            int length = array.length();
                            if (length > 0) {
                                Base base;
                                for (int i = 0; i < length; i++) {
                                    base = new Base();
                                    base.setTitle(Tool.getStringJson("title", array.getJSONObject(i)));
                                    base.setDescription(Tool.getStringJson("content", array.getJSONObject(i)));
                                    base.setFeature(Tool.getStringJson("images", array.getJSONObject(i)));
                                    base.setDatetime(Tool.getStringJson("createvote", array.getJSONObject(i)));
                                    base.setRating(Tool.getStringJson("rating", array.getJSONObject(i)));
                                    base.setType(Tool.getStringJson("username", array.getJSONObject(i)));
                                    datas.add(base);
                                }
                            } else
                                isMoreDataAvailable = false;

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    if (isAdded())
                        ToastCustom.show(getActivity(), getResources().getString(R.string.please_retry_later) + " (error" + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (isAdded())
                        ToastCustom.show(getActivity(), getResources().getString(R.string.please_retry_later) + " (error" + String.valueOf(statusCode) + ")", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onFinish() {
                    isSending = false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgToolbarBack:
                dismiss();
                break;
        }
    }
}
