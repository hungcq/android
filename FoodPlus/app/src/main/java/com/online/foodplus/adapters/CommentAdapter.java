package com.online.foodplus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.online.foodplus.R;
import com.online.foodplus.models.Comment;

import java.util.ArrayList;

/**
 * Created by thanhthang on 28/12/2016.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Comment> datas;
    private static final int LOAD = 0;
    private static final int VIEW = 1;

    public CommentAdapter(ArrayList<Comment> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        } else if (holder instanceof MyViewHolder)
            ((MyViewHolder) holder).bindData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (datas.get(position) == null)
            return LOAD;
        else
            return VIEW;
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircularImageView imgFeature;
        private TextView tvTitle, tvDescription;
        private ImageView imgPhotoAttach;

        MyViewHolder(View itemView) {
            super(itemView);
            imgPhotoAttach = (ImageView) itemView.findViewById(R.id.imgPhotoAttach);
            imgFeature = (CircularImageView) itemView.findViewById(R.id.imgFeature);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        }

        void bindData(Comment comment) {
            tvTitle.setText(comment.getUsername());
            if (comment.getContent() != null && !comment.getContent().equals("null") && !comment.getContent().trim().equals("")) {
                tvDescription.setText(comment.getContent());
                tvDescription.setVisibility(View.VISIBLE);
            } else
                tvDescription.setVisibility(View.GONE);
            if (comment.getAvatar() != null && !comment.getAvatar().equals("null") && !comment.getAvatar().trim().equals(""))
                Glide.with(imgFeature.getContext()).load(comment.getAvatar())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgFeature);
                //                Picasso.with(imgFeature.getContext())
                //                        .load(comment.getAvatar())
                //                        .resize(180, 180)
                //                        .centerCrop().into(imgFeature);
            else imgFeature.setImageResource(R.drawable.ic_user_round);
            if (comment.getComment_photo() != null && !comment.getComment_photo().equals("null") && !comment.getComment_photo().trim().equals("")) {
                imgPhotoAttach.setVisibility(View.VISIBLE);
                Glide.with(imgPhotoAttach.getContext()).load(comment.getComment_photo())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgPhotoAttach);
                //                Picasso.with(imgPhotoAttach.getContext())
                //                        .load(comment.getComment_photo())
                //                        .resize(0, 400)
                //                        //                         .centerCrop()
                //                        .into(imgPhotoAttach);
            } else
                imgPhotoAttach.setVisibility(View.GONE);

        }
    }
}
