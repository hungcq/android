package com.online.foodplus.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.foodplus.R;
import com.online.foodplus.models.Base;
import com.online.foodplus.widgets.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.online.foodplus.R.id.tvNumber;

/**
 * Created by thanhthang on 06/12/2016.
 */

public class MediaAdapter extends BaseAdapter {
    private ArrayList<Base> datas;
    private String total;

    public MediaAdapter(ArrayList<Base> datas, String total) {
        this.datas = datas;
        this.total = total;
    }

    public MediaAdapter(ArrayList<Base> datas) {
        this.datas = datas;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_media, viewGroup, false);
            holder = new ViewHolder();
            holder.tvNumber = (TextView) view.findViewById(tvNumber);
            holder.imgVideo = (ImageView) view.findViewById(R.id.imgVideo);
            holder.imgFeature = (SquareImageView) view.findViewById(R.id.imgFeature);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        if ((position == (datas.size() - 1)) && (total != null)) {
            holder.tvNumber.setVisibility(View.VISIBLE);
            holder.tvNumber.setText(total);
        } else if ((datas.get(position).getType() != null) && (datas.get(position).getType().equals("video")))
            holder.imgVideo.setVisibility(View.VISIBLE);
        else
            holder.imgVideo.setVisibility(View.GONE);

        Picasso.with(viewGroup.getContext())
                .load(datas.get(position).getFeature())
                .resize(180, 180)
                .centerCrop().into(holder.imgFeature);
        return view;
    }

    private static class ViewHolder {
        SquareImageView imgFeature;
        ImageView imgVideo;
        TextView tvNumber;
    }
}
