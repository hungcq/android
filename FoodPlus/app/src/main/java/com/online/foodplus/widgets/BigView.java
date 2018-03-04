package com.online.foodplus.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.online.foodplus.R;
import com.online.foodplus.activities.DetailActivity;
import com.online.foodplus.adapters.BigViewAdapter;
import com.online.foodplus.libraries.GridSpacingItemDecoration;
import com.online.foodplus.libraries.RecyclerItemClickListener;
import com.online.foodplus.models.Base;

import java.util.ArrayList;

/**
 * Created by thanhthang on 06/12/2016.
 */

public class BigView extends LinearLayout {
    private RecyclerView rvContent;
    private ArrayList<Base> datas;
    private BigViewAdapter adapter;

    public BigView(Context context) {
        super(context);
        init(context, null);
    }

    public BigView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setData(ArrayList<Base> datas) {
        if (datas == null || rvContent == null) return;
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    public void addData(Base base) {
        this.datas.add(base);
        adapter.notifyDataSetChanged();
    }


    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.holder_content, this);
            rvContent = (RecyclerView) findViewById(R.id.rvContent);
            rvContent.setNestedScrollingEnabled(false);
            rvContent.setLayoutManager(new LinearLayoutManager(context));
            datas = new ArrayList<>();
            adapter = new BigViewAdapter(this.datas);
            rvContent.addItemDecoration(new GridSpacingItemDecoration(1, (int) context.getResources().getDimension(R.dimen._3sdp), false));
            rvContent.setAdapter(adapter);
            rvContent.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", datas.get(position).getId());
                    intent.putExtra("t", datas.get(position).getTid());
                    intent.putExtra("cid", datas.get(position).getCid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
                    context.startActivity(intent);

                    //Animation if Activity
                    if (context instanceof Activity)
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }));
        }
    }
}
