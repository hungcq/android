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

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by thanhthang on 06/12/2016.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Base> datas;

    public ReviewListAdapter(ArrayList<Base> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_review2, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).bindData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgFeature;
        private TextView tvUsername, tvTitle, tvDescription, tvDatetime;
        private MaterialRatingBar ratingBar;

        MyViewHolder(View itemView) {
            super(itemView);
            imgFeature = (ImageView) itemView.findViewById(R.id.imgFeature);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvDatetime = (TextView) itemView.findViewById(R.id.tvDatetime);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ratingBar = (MaterialRatingBar) itemView.findViewById(R.id.ratingBar);
        }

        void bindData(Base base) {
            if (base.getTitle() != null && !base.getTitle().equals("null"))
                tvTitle.setText(base.getTitle());

            if (base.getType() != null && !base.getType().equals("null"))
                tvUsername.setText(base.getType());

            if (base.getDatetime() != null && !base.getDatetime().equals("null"))
                tvDatetime.setText(base.getDatetime());

            if (base.getDescription() != null && !base.getDescription().equals("null"))
                tvDescription.setText(base.getDescription());

            if (base.getRating() != null && !base.getRating().equals("null")) {
                ratingBar.setRating(Float.valueOf(base.getRating()));
                ratingBar.setVisibility(View.VISIBLE);
            }

            if (base.getFeature() != null && !base.getFeature().equals("http://dev.xplay.vn/img/null") && !base.getFeature().equals("http://dev.xplay.vn/img/"))
                Glide.with(imgFeature.getContext()).load(base.getFeature())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgFeature);
            //                Picasso.with(imgFeature.getContext())
            //                        .load(base.getFeature())
            //                        .resize(180, 180)
            //                        .centerCrop().into(this.imgFeature);//avatar
        }
    }
}
