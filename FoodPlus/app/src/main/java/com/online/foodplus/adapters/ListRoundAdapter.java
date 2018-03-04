package com.online.foodplus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.models.Base;
import com.online.foodplus.utils.Tool;

import java.util.ArrayList;

/**
 * Created by thanhthang on 28/12/2016.
 */

public class ListRoundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Base> datas;
    private boolean hideRating = false;

    public ListRoundAdapter(ArrayList<Base> datas) {
        this.datas = datas;
    }

    public ListRoundAdapter(ArrayList<Base> datas, boolean hideRating) {
        this.hideRating = hideRating;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_round, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).bindData(datas.get(position), hideRating);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircularImageView imgFeature;
        private TextView tvTitle, tvRating, tvDescription, tvDatetime;

        MyViewHolder(View itemView) {
            super(itemView);
            imgFeature = (CircularImageView) itemView.findViewById(R.id.imgFeature);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            tvDatetime = (TextView) itemView.findViewById(R.id.tvDatetime);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        }

        void bindData(Base base, boolean hideRating) {
            if (base.getTitle() != null)
                tvTitle.setText(base.getTitle());
            if (base.getDatetime() != null) {
                tvDatetime.setText(base.getDatetime());
                tvDatetime.setVisibility(View.VISIBLE);
            }

            if (base.getDescription() != null)
                tvDescription.setText(base.getDescription());
            if (hideRating)
                tvRating.setVisibility(View.GONE);
            else {
                tvRating.setVisibility(View.VISIBLE);
                if (base.getRating() != null)
                    tvRating.setText(base.getRating());
            }

            if (base.getImages() != null && base.getImages().size() > 0)
                Glide.with(imgFeature.getContext()).load(Tool.findImageUrl(base.getImages().get(0), Constants.IMAGE_SMALL))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgFeature);
            //                Picasso.with(imgFeature.getContext())
            //                        .load(Tool.findImageUrl(base.getImages().get(0), Constants.IMAGE_SMALL))
            //                        .into(imgFeature);
        }
    }
}
