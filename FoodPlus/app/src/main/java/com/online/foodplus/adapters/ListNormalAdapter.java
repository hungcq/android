package com.online.foodplus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.models.Base;
import com.online.foodplus.utils.Tool;

import java.util.ArrayList;

/**
 * Created by thanhthang on 28/12/2016.
 */

public class ListNormalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Base> datas;

    public ListNormalAdapter(ArrayList<Base> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_normal, parent, false));
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
        private TextView tvTitle, tvRating, tvDescription;

        MyViewHolder(View itemView) {
            super(itemView);
            imgFeature = (ImageView) itemView.findViewById(R.id.imgFeature);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        }

        void bindData(Base base) {
            if (base.getTitle() != null)
                tvTitle.setText(base.getTitle());
            if (base.getDescription() != null && !base.getDescription().equals("null"))
                this.tvDescription.setText(base.getDescription());
            if (base.getRating() != null)
                tvRating.setText(base.getRating());
            if (base.getImages() != null && base.getImages().size() > 0)
                Glide.with(imgFeature.getContext()).load(Tool.findImageUrl(base.getImages().get(0), Constants.IMAGE_SMALL))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgFeature);
            //                            Picasso.with(imgFeature.getContext())
            //                                    .load(Tool.findImageUrl(base.getImages().get(0), Constants.IMAGE_SMALL))
            //                                    .into(this.imgFeature);
        }
    }
}
