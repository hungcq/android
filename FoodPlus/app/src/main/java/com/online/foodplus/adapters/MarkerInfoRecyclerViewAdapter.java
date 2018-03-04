package com.online.foodplus.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.online.foodplus.R;
import com.online.foodplus.models.Base2;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 1918 on 19-Dec-16.
 */

public class MarkerInfoRecyclerViewAdapter extends RecyclerView.Adapter<MarkerInfoRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Base2> jsonPlaceModelList;

    public MarkerInfoRecyclerViewAdapter(List<Base2> jsonPlaceModelList) {
        this.jsonPlaceModelList = jsonPlaceModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_marker_info, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Bundle bundle = new Bundle();
                //                bundle.putString("id", String.valueOf(jsonPlaceModelList.get(myViewHolder.getAdapterPosition()).getId()));
                //                bundle.putString("tid", String.valueOf(jsonPlaceModelList.get(myViewHolder.getAdapterPosition()).getTid()));
                //                bundle.putString("cid", String.valueOf(RealmHandler.getInst().findCategoryByT(jsonPlaceModelList.get(myViewHolder
                //                        .getAdapterPosition()).getTid()).getCid()));
                //                context.startActivity(new Intent(context, DetailActivity.class).putExtras(bundle));
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Base2 place = jsonPlaceModelList.get(position);
        if (place.getImages() != null)
            Glide.with(context).load(place.getImages().get(0))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.itemImage);
        //            Picasso.with(context).load(place.getImages().get(0))
        //                    .resize(200, 200).centerCrop().into(holder.itemImage);
        if (place.getTitle() != null)
            holder.title.setText(place.getTitle());
        if (place.getDescription() != null)
            holder.location.setText(place.getDescription());
        //        holder.favourite.setText(String.valueOf(place.getFavourite()));
        //        holder.star.setText(String.valueOf(place.getStar()));
        //        holder.comment.setText(String.valueOf(place.getComment()));
        //        holder.pin.setText(String.valueOf(place.getPin()));
        //        holder.rating.setText(String.valueOf(String.valueOf(place.getRating())));
        Double distance = place.getDistance();
        DecimalFormat decimalFormar = new DecimalFormat("0.0");
        holder.distance.setText(decimalFormar.format(distance) + " km");
    }

    @Override
    public int getItemCount() {
        return jsonPlaceModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView title;
        TextView location;
        TextView favourite, star, comment, pin;
        TextView distance;
        TextView rating;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            title = (TextView) itemView.findViewById(R.id.item_title);
            location = (TextView) itemView.findViewById(R.id.item_location);
            favourite = (TextView) itemView.findViewById(R.id.item_favourite);
            star = (TextView) itemView.findViewById(R.id.item_star);
            comment = (TextView) itemView.findViewById(R.id.item_comment);
            pin = (TextView) itemView.findViewById(R.id.item_pin);
            distance = (TextView) itemView.findViewById(R.id.item_distance);
            rating = (TextView) itemView.findViewById(R.id.item_rating);
        }
    }
}
