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

/**
 * Created by thanhthang on 21/12/2016.
 */

public class FindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Base> datas;

    public FindAdapter(ArrayList<Base> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new FindViewHolder(inflater.inflate(R.layout.row_list_square, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FindViewHolder) holder).bindData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private static class FindViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDescription, tvComment, tvPin, tvStar, tvFavourite, tvRating;
        private ImageView imgFeature;

        FindViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvPin = (TextView) itemView.findViewById(R.id.tvPin);
            tvStar = (TextView) itemView.findViewById(R.id.tvStar);
            tvFavourite = (TextView) itemView.findViewById(R.id.tvFavourite);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            imgFeature = (ImageView) itemView.findViewById(R.id.imgFeature);
        }

        void bindData(Base base) {
            try {
                if (base.getTitle() != null && !base.getTitle().equals("null"))
                    tvTitle.setText(base.getTitle());
                if (base.getDescription() != null && !base.getDescription().equals("null"))
                    tvDescription.setText(base.getDescription());
                if (base.getComment() != null)
                    tvComment.setText(base.getComment());
                if (base.getPin() != null)
                    tvPin.setText(base.getPin());
                if (base.getFavourite() != null)
                    tvFavourite.setText(base.getFavourite());
                if (base.getStar() != null)
                    tvStar.setText(base.getStar());
                if (base.getRating() != null)
                    tvRating.setText(base.getRating());
                if (base.getImages() != null && base.getImages().size() > 0)
                    Glide.with(imgFeature.getContext()).load(base.getImages().get(0))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgFeature);
                //                    Picasso.with(imgFeature.getContext())
                //                            .load(base.getImages().get(0))
                //                            .into(imgFeature);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }
}
