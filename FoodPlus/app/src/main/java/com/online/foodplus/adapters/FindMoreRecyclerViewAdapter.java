package com.online.foodplus.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.online.foodplus.R;

import java.util.List;

/**
 * Created by 1918 on 20-Dec-16.
 */

public class FindMoreRecyclerViewAdapter extends RecyclerView.Adapter<FindMoreRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<String> imageList;

    public FindMoreRecyclerViewAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_find_more, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //        Picasso.with(context).load(imageList.get(position)).resize(500, 200).centerCrop().into(holder.imageView);
        Glide.with(context).load(imageList.get(position))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (RoundedImageView) itemView.findViewById(R.id.item_image);
            textView = (TextView) itemView.findViewById(R.id.item_text);
        }
    }
}
