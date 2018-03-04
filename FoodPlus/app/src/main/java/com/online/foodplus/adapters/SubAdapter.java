package com.online.foodplus.adapters;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.online.foodplus.R;
import com.online.foodplus.models.Base;

import java.util.ArrayList;

/**
 * Created by thanhthang on 25/11/2016.
 */

public class SubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Base> datas;

    public SubAdapter(ArrayList<Base> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sub, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (datas.get(position) != null)
            ((MyViewHolder) holder).bindData(datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        View vIndicator;

        MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            vIndicator = itemView.findViewById(R.id.vIndicator);
        }

        void bindData(Base base, int position) {
            if (base.getTitle() != null && !base.getTitle().equals("null")) {
                tvTitle.setText(base.getTitle());
                if (base.getType() != null)
                    if (base.getType().equals("active"))
                        tvTitle.setTypeface(null, Typeface.BOLD);
                    else
                        tvTitle.setTypeface(null, Typeface.NORMAL);
                else
                    tvTitle.setTypeface(null, Typeface.NORMAL);
            }
            if (position % 6 == 0)
                vIndicator.setBackground(ContextCompat.getDrawable(vIndicator.getContext(), R.drawable.bg_circle_green));
            else if (position % 5 == 0)
                vIndicator.setBackground(ContextCompat.getDrawable(vIndicator.getContext(), R.drawable.bg_circle_red));
            else if (position % 4 == 0)
                vIndicator.setBackground(ContextCompat.getDrawable(vIndicator.getContext(), R.drawable.bg_circle_purple));
            else if (position % 3 == 0)
                vIndicator.setBackground(ContextCompat.getDrawable(vIndicator.getContext(), R.drawable.bg_circle_orange));
            else if (position % 2 == 0)
                vIndicator.setBackground(ContextCompat.getDrawable(vIndicator.getContext(), R.drawable.bg_circle_yellow));
            else
                vIndicator.setBackground(ContextCompat.getDrawable(vIndicator.getContext(), R.drawable.bg_circle_blue));
        }
    }
}
