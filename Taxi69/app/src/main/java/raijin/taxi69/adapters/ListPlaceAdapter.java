package raijin.taxi69.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import raijin.taxi69.R;
import raijin.taxi69.models.jsonplacesmodels.Result;

/**
 * Created by 1918 on 20-Jan-17.
 */

public class ListPlaceAdapter extends RecyclerView.Adapter<ListPlaceAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Result> placesList;

    public ListPlaceAdapter(List<Result> placesList) {
        this.placesList = placesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_rv_place, parent, false);
        view.setOnClickListener(this);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (placesList.get(position).getPhotos() != null) {
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&maxheight=100&photoreference="
                    + placesList.get(position).getPhotos().get(0).getPhotoReference()
                    + "&key=" + context.getString(R.string.api_key);
            Picasso.with(context).load(photoUrl).into(holder.imageView);
        }
        holder.name.setText(placesList.get(position).getName());
        holder.location.setText(placesList.get(position).getVicinity());
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    @Override
    public void onClick(View v) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        TextView location;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            name = (TextView) itemView.findViewById(R.id.item_name);
            location = (TextView) itemView.findViewById(R.id.item_location);
        }
    }
}
