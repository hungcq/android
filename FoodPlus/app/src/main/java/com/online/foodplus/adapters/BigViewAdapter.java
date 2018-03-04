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

public class BigViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Base> datas;

    public BigViewAdapter(ArrayList<Base> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bigview, parent, false));
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
        private ImageView imgFeature, imgMap;
        private TextView tvTitle, tvRating, tvDescription, tvStar, tvFavourite, tvComment, tvPin;

        MyViewHolder(View itemView) {
            super(itemView);
            imgFeature = (ImageView) itemView.findViewById(R.id.imgFeature);
            imgMap = (ImageView) itemView.findViewById(R.id.imgMap);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvStar = (TextView) itemView.findViewById(R.id.tvStar);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvPin = (TextView) itemView.findViewById(R.id.tvPin);
            tvFavourite = (TextView) itemView.findViewById(R.id.tvFavourite);
        }

        void bindData(Base base) {
            if (base.getTitle() != null)
                tvTitle.setText(base.getTitle());
            if (base.getRating() != null)
                tvRating.setText(base.getRating());
            String description = base.getDescription();
            if (description != null && !description.equals("null")) {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(base.getDescription() != null ? base.getDescription() : "");
            }
            if (base.getStar() != null)
                tvStar.setText(base.getStar());
            if (base.getFavourite() != null)
                tvFavourite.setText(base.getFavourite());
            if (base.getComment() != null)
                tvComment.setText(base.getComment());
            if (base.getPin() != null)
                tvPin.setText(base.getPin());
            if (base.getImages() != null && base.getImages().size() > 0) {
                //http://stackoverflow.com/questions/9279111/determine-if-the-device-is-a-smartphone-or-tablet
                boolean tabletSize = tvTitle.getContext().getResources().getBoolean(R.bool.isTablet);
                if (tabletSize)
                    Glide.with(imgFeature.getContext()).load(Tool.findImageUrl(base.getImages().get(0), Constants.IMAGE_BIG))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgFeature);
                    //                    Picasso.with(imgFeature.getContext()).load(Tool.findImageUrl(base.getImages().get(0), Constants.IMAGE_BIG)).into(this.imgFeature);
                else
                    Glide.with(imgFeature.getContext()).load(Tool.findImageUrl(base.getImages().get(0), Constants.IMAGE_NORMAL))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgFeature);
                //                    Picasso.with(imgFeature.getContext()).load(Tool.findImageUrl(base.getImages().get(0), Constants.IMAGE_NORMAL)).into(imgFeature);

            }

        }
    }
}
