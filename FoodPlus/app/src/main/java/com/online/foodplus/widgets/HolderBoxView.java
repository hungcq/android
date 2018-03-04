package com.online.foodplus.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.online.foodplus.R;
import com.online.foodplus.interfaces.HolderBoxItemClickListener;

/**
 * Created by thanhthang on 14/12/2016.
 */

public class HolderBoxView extends LinearLayout implements View.OnClickListener {
    private TextView tvTitleBox, tvReadMore, tvLoadMore;
    private LinearLayout holderParent;
    private HolderBoxItemClickListener itemClickListener;
    private int id;
    public static final int MODE_VISIBLE_ALL = 1;
    public static final int MODE_VISIBLE_READ_MORE = 2;
    public static final int MODE_VISIBLE_LOAD_MORE = 3;
    public static final int MODE_GONE = 0;

    public HolderBoxView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.holder_box, this);
            holderParent = (LinearLayout) findViewById(R.id.holderParent);
            tvTitleBox = (TextView) findViewById(R.id.tvTitleBox);
            tvReadMore = (TextView) findViewById(R.id.tvReadMore);
            tvLoadMore = (TextView) findViewById(R.id.tvLoadMore);

            tvReadMore.setOnClickListener(this);
            tvLoadMore.setOnClickListener(this);

        }
    }

    public void setData(String title) {
        this.tvTitleBox.setText(title);
    }


    public void setData(String title, int id, int type) {
        this.tvTitleBox.setText(title);
        this.tvTitleBox.setVisibility(View.VISIBLE);
        this.id = id;
        setType(type);
    }

    public void setData(String title, int type) {
        this.tvTitleBox.setText(title);
        setType(type);
    }

    public void setType(int type) {
        switch (type) {
            case MODE_GONE:
                tvLoadMore.setVisibility(View.GONE);
                tvReadMore.setVisibility(View.GONE);
                break;
            case MODE_VISIBLE_ALL:
                tvLoadMore.setVisibility(View.VISIBLE);
                tvReadMore.setVisibility(View.VISIBLE);
                break;
            case MODE_VISIBLE_LOAD_MORE:
                tvLoadMore.setVisibility(View.VISIBLE);
                break;
            case MODE_VISIBLE_READ_MORE:
                tvReadMore.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void addView(View view) {
        holderParent.addView(view);
    }

    public void setItemClickListener(HolderBoxItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvReadMore:
                itemClickListener.readMore(id);
                break;
            case R.id.tvLoadMore:
                itemClickListener.loadMore(id);
                break;
        }
    }
}
