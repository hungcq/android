package com.online.foodplus.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Base> datas;
    private static final int BIG = 0;
    private static final int NORMAL = 1;

    public EventAdapter(ArrayList<Base> datas) {
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {

        if (datas.get(position).getType() != null && datas.get(position).getType().equals("big"))
            return BIG;

        return NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case BIG:
                return new BigViewHolder(inflater.inflate(R.layout.row_event, parent, false));
        }
        return new NormalViewHolder(inflater.inflate(R.layout.row_event2, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case NORMAL:
                ((NormalViewHolder) holder).bindData(datas.get(position));
                break;
            case BIG:
                ((BigViewHolder) holder).bindData(datas.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private static class NormalViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgFeature;
        private TextView tvTitle, tvDatetime, tvDescription;
        private Button bDetail, bShare;

        NormalViewHolder(View itemView) {
            super(itemView);
            imgFeature = (ImageView) itemView.findViewById(R.id.imgFeature);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDatetime = (TextView) itemView.findViewById(R.id.tvDatetime);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
     /*       bDetail = (Button) itemView.findViewById(R.id.bDetail);
            bShare = (Button) itemView.findViewById(R.id.bShare);*/
        }

        void bindData(final Base event) {
            tvTitle.setText(event.getTitle());
            if (event.getDescription() != null && !event.getDescription().equals("null"))
                tvDescription.setText(event.getDescription());
            tvDatetime.setText(event.getDatetime());
            if (event.getImages() != null && event.getImages().size() > 0)
                Glide.with(imgFeature.getContext()).load(event.getImages().get(0))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgFeature);
            //                Picasso.with(imgFeature.getContext())
            //                        .load(event.getImages().get(0))
            //                        .into(this.imgFeature);
        }
    }

    private static class BigViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgFeature;
        private TextView tvTitle, tvDatetime, tvDescription;
        private Button bDetail, bShare;

        BigViewHolder(View itemView) {
            super(itemView);
            imgFeature = (ImageView) itemView.findViewById(R.id.imgFeature);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDatetime = (TextView) itemView.findViewById(R.id.tvDatetime);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            bDetail = (Button) itemView.findViewById(R.id.bDetail);
            bShare = (Button) itemView.findViewById(R.id.bShare);
        }

        void bindData(final Base event) {
            if (event.getTitle() != null && !event.getTitle().equals("null"))
                tvTitle.setText(event.getTitle());
            if (event.getDescription() != null && !event.getDescription().equals("null"))
                tvDescription.setText(event.getDescription());
            if (event.getDatetime() != null)
                tvDatetime.setText(event.getDatetime());
            if (event.getImages() != null && event.getImages().size() > 0)
                //                Picasso.with(imgFeature.getContext())
                //                        .load(Tool.findImageUrl(event.getImages().get(0), Constants.IMAGE_BIG))
                //                        .into(this.imgFeature);
                Glide.with(imgFeature.getContext()).load(Tool.findImageUrl(event.getImages().get(0), Constants.IMAGE_BIG))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgFeature);
            //            bShare.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            //                    sharingIntent.setType("text/plain");
            //                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, event.getTitle());
            //                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, event.getTitle() + ": " + event.getDescription());
            //                    tvTitle.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));
            //                }
            //            });
            //            bDetail.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    ToastCustom.show(tvTitle.getContext(), "Tính năng này chưa sẵn sàng", ToastCustom.LENGTH_SHORT, ToastCustom.INF0);
            //                }
            //            });
        }
    }

    public void removeItem(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, datas.size());
    }
}
