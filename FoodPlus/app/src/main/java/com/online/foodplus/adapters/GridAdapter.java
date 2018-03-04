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

import java.util.List;

/**
 * Created by thanhthang on 06/12/2016.
 */
public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Base> datas;

    public GridAdapter(List<Base> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grid, parent, false));
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
        private TextView tvTitle, tvDescription, tvStar, tvFavourite, tvComment, tvPin;

        MyViewHolder(View itemView) {
            super(itemView);
            imgFeature = (ImageView) itemView.findViewById(R.id.imgFeature);
            imgMap = (ImageView) itemView.findViewById(R.id.imgMap);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvStar = (TextView) itemView.findViewById(R.id.tvStar);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvPin = (TextView) itemView.findViewById(R.id.tvPin);
            tvFavourite = (TextView) itemView.findViewById(R.id.tvFavourite);
        }

        void bindData(Base base) {
            if (base.getTitle() != null)
                tvTitle.setText(base.getTitle());
            String description = base.getDescription();
            if (description != null && !base.getDescription().equals("null")) {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(description);
            }
            if (base.getStar() != null)
                tvStar.setText(base.getStar());
            if (base.getFavourite() != null)
                tvFavourite.setText(base.getFavourite());
            if (base.getComment() != null)
                tvComment.setText(base.getComment());
            if (base.getPin() != null)
                tvPin.setText(base.getPin());
            if (base.getImages() != null && base.getImages().size() > 0)
                Glide.with(imgFeature.getContext()).load(base.getImages().get(0))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgFeature);
            //                Picasso.with(imgFeature.getContext())
            //                        .load(base.getImages().get(0))
            //                        .into(imgFeature);
        }
    }
}
