package com.online.foodplus.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.online.foodplus.R;
import com.online.foodplus.adapters.MediaAdapter;
import com.online.foodplus.models.Base;
import com.online.foodplus.utils.Tool;

import java.util.ArrayList;

/**
 * Created by thanhthang on 16/12/2016.
 */

public class MediaView extends LinearLayout {
    private ExpandableHeightGridView gvGridSimple;
    private ArrayList<Base> datas;
    private MediaAdapter adapter;

    public MediaView(Context context) {
        super(context);
        init(context, null);
    }

    public MediaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void addData(Base base) {
        this.datas.add(base);
        adapter.notifyDataSetChanged();
    }

    public void setTotal(String total) {
        adapter.setTotal(total);
        adapter.notifyDataSetChanged();
    }

    public void setData(ArrayList<Base> datas, String total) {
        if (gvGridSimple == null || datas.size() == 0) return;
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.setTotal(total);
        adapter.notifyDataSetChanged();
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_media, this);
            gvGridSimple = (ExpandableHeightGridView) findViewById(R.id.gvGridSimple);
            datas = new ArrayList<>();
            gvGridSimple.setExpanded(true);
            adapter = new MediaAdapter(datas);
            gvGridSimple.setAdapter(adapter);
            gvGridSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Tool.showToast(context, "Click: " + String.valueOf(datas.get(position).getId()));
                }
            });
        }
    }
}
