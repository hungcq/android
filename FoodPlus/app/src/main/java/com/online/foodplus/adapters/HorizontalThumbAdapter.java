package com.online.foodplus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.utils.Tool;

import java.util.ArrayList;

/**
 * Created by thanhthang on 25/11/2016.
 */

public class HorizontalThumbAdapter extends RecyclerView.Adapter<HorizontalThumbAdapter.MyViewHolder> {
    private ArrayList<String> listThumb;

    public HorizontalThumbAdapter(ArrayList<String> listThumb) {
        this.listThumb = listThumb;
        //        if (this.listThumb.size() > 0)
        //            this.listThumb.remove(0);
    }

    @Override
    public HorizontalThumbAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_thumb, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalThumbAdapter.MyViewHolder holder, int position) {
        if (listThumb.get(position) != null)
            Glide.with(holder.imgItemThumb.getContext()).load(Tool.findImageUrl(listThumb.get(position), Constants.IMAGE_SMALL))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgItemThumb);
        //            Picasso.with(holder.imgItemThumb.getContext())
        //                    .load(Tool.findImageUrl(listThumb.get(position), Constants.IMAGE_SMALL))
        //                    .into(holder.imgItemThumb);
    }

    @Override
    public int getItemCount() {
        return listThumb.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgItemThumb;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgItemThumb = (RoundedImageView) itemView.findViewById(R.id.imgItemThumb);
        }
    }
}
