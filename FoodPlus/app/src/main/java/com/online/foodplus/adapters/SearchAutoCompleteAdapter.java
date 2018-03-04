package com.online.foodplus.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.R;
import com.online.foodplus.models.Base;
import com.online.foodplus.utils.Tool;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.online.foodplus.R.id.imgFeature;

/**
 * Created by thanhthang on 26/12/2016.
 */

public class SearchAutoCompleteAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Base> datas;
    private boolean isSending = false;
    private Context context;

    public SearchAutoCompleteAdapter(Context context) {
        datas = new ArrayList<>();
        this.context = context;
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

    private static class ViewHolder {
        TextView tvTitle, tvDescription, tvComment, tvPin, tvStar, tvFavourite, tvRating;
        ImageView imgFeature;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_square, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            holder.tvDescription = (TextView) view.findViewById(R.id.tvDescription);
            holder.tvComment = (TextView) view.findViewById(R.id.tvComment);
            holder.tvPin = (TextView) view.findViewById(R.id.tvPin);
            holder.tvStar = (TextView) view.findViewById(R.id.tvStar);
            holder.tvFavourite = (TextView) view.findViewById(R.id.tvFavourite);
            holder.tvRating = (TextView) view.findViewById(R.id.tvRating);
            holder.imgFeature = (ImageView) view.findViewById(imgFeature);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        if (datas.get(position).getTitle() != null)
            holder.tvTitle.setText(datas.get(position).getTitle());

        if (datas.get(position).getDescription() != null && !datas.get(position).getDescription().equals("null"))
            holder.tvDescription.setText(datas.get(position).getDescription());
        if (datas.get(position).getComment() != null)
            holder.tvComment.setText(datas.get(position).getComment());
        if (datas.get(position).getPin() != null)
            holder.tvPin.setText(datas.get(position).getPin());
        if (datas.get(position).getFavourite() != null)
            holder.tvFavourite.setText(datas.get(position).getFavourite());
        if (datas.get(position).getStar() != null)
            holder.tvStar.setText(datas.get(position).getStar());
        holder.tvRating.setVisibility(View.GONE);
        if (datas.get(position).getImages() != null && datas.get(position).getImages().size() > 0)
            Glide.with(context).load(datas.get(position).getImages().get(0))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgFeature);
        //            Picasso.with(context)
        //                    .load(datas.get(position).getImages().get(0))
        //                    .into(holder.imgFeature);
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    //Get data from the Server
                    String term = constraint.toString();
                    //System.out.println("----------------------" + term);
                    if (!isSending) {
                        isSending = true;
                        RequestParams params = new RequestParams();
                        params.put("key", term);
                        params.put("p", 1);
                        params.put("scr", "200x200");
                        SharedPreferences pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                        String cityID = pref.getString("city_id", "");
                        if (!cityID.equals(""))
                            params.put("city", cityID);
                        Tool.get(context.getResources().getString(R.string.api_search), params, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                datas.clear();
                                int length = response.length();
                                for (int i = 0; i < length; i++) {
                                    try {
                                        datas.add(Tool.readObject(response.getJSONObject(i)));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFinish() {
                                isSending = false;
                            }
                        });
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //Nothing to do
            }
        };

    }
}
