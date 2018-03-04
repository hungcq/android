package com.online.foodplus.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.online.foodplus.R;
import com.online.foodplus.activities.DetailActivity;
import com.online.foodplus.adapters.ReviewAdapter;
import com.online.foodplus.dialogs.ReviewDialog;
import com.online.foodplus.libraries.GridSpacingItemDecoration;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.models.Base;

import java.util.ArrayList;

/**
 * Created by thanhthang on 14/12/2016.
 */

public class ListReview extends LinearLayout implements View.OnClickListener {
    private RecyclerView rvContent;
    private ArrayList<Base> datas;
    private ReviewAdapter adapter;
    private TextView tvReview, tvNote;
    private Context context;
    private String title, description, feature, id, cid, t, uid;

    public ListReview(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public ListReview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public void setData(ArrayList<Base> datas) {
        if (datas == null || rvContent == null) return;

        if (datas.size() > 0) {
            if (tvNote.getVisibility() == View.VISIBLE)
                tvNote.setVisibility(View.GONE);
            if (rvContent.getVisibility() == View.GONE)
                rvContent.setVisibility(View.VISIBLE);
            if (tvReview.getVisibility() == View.GONE)
                tvReview.setVisibility(View.VISIBLE);
        }
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    public void setInfo(String inputTitle, String inputDescription, String inputFeature, String inputId, String inputT, String inputCid, String inputUid) {
        this.id = inputId;
        this.t = inputT;
        this.cid = inputCid;
        this.uid = inputUid;
        this.title = inputTitle;
        this.description = inputDescription;
        this.feature = inputFeature;
    }

    public void addData(Base base) {
        this.datas.add(base);
        adapter.notifyDataSetChanged();
        if (tvNote.getVisibility() == View.VISIBLE)
            tvNote.setVisibility(View.GONE);
        if (rvContent.getVisibility() == View.GONE)
            rvContent.setVisibility(View.VISIBLE);
        if (tvReview.getVisibility() == View.GONE)
            tvReview.setVisibility(View.VISIBLE);
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_list_review, this);
            datas = new ArrayList<>();
            adapter = new ReviewAdapter(this.datas);

            tvReview = (TextView) findViewById(R.id.tvReview);
            tvNote = (TextView) findViewById(R.id.tvNote);
            rvContent = (RecyclerView) findViewById(R.id.rvContent);

            rvContent.setLayoutManager(new GridLayoutManager(context, 2));
            rvContent.setAdapter(adapter);
            rvContent.addItemDecoration(new GridSpacingItemDecoration(2, (int) context.getResources().getDimension(R.dimen._5sdp), false));
            rvContent.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", datas.get(position).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                    context.startActivity(intent);

                    //Animation if Activity
                    if (context instanceof Activity)
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }));

            tvReview.setOnClickListener(this);
            tvNote.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReview:
            case R.id.tvNote:
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                FragmentManager fm = fragmentActivity.getSupportFragmentManager();
                DialogFragment newFragment = ReviewDialog.newInstance(title,description,feature,id, t, cid, uid);
                newFragment.show(fm, "dialogReview");
                break;
        }
    }
}