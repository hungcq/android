package com.online.foodplus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.online.foodplus.R;
import com.online.foodplus.models.Base;
import com.online.foodplus.widgets.SquareImageView;

import java.util.ArrayList;

/**
 * Created by thanhthang on 06/12/2016.
 */

public class GridSimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Base> datas;
    private boolean isPin = false;

    public GridSimpleAdapter(ArrayList<Base> datas) {
        this.datas = datas;
    }

    public GridSimpleAdapter(ArrayList<Base> datas, boolean isPin) {
        this.datas = datas;
        this.isPin = isPin;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grid_simple, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isPin)
            ((MyViewHolder) holder).bindData(datas.get(position), isPin);
        else
            ((MyViewHolder) holder).bindData(datas.get(position));

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private SquareImageView imgFeature;
        private TextView tvTitle;
        private ImageView imgPin;

        MyViewHolder(View itemView) {
            super(itemView);
            imgFeature = (SquareImageView) itemView.findViewById(R.id.imgFeature);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            imgPin = (ImageView) itemView.findViewById(R.id.imgPin);
        }

        void bindData(Base base) {
            setData(base, false);
        }

        void bindData(Base base, boolean isPin) {
            setData(base, isPin);
        }

        private void setData(Base base, boolean isPin) {
            if (base.getTitle() != null && !base.getTitle().equals("null"))
                tvTitle.setText(base.getTitle());
            if (base.getImages() != null && base.getImages().size() > 0)
                //                Picasso.with(imgFeature.getContext())
                //                        .load(base.getImages().get(0))
                //                        .into(imgFeature);
                Glide.with(imgFeature.getContext()).load(base.getImages().get(0))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgFeature);
            if (isPin)
                imgPin.setVisibility(View.VISIBLE);

        }
    }
}