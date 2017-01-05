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
import raijin.taxi69.models.TaxiType;

/**
 * Created by 1918 on 27-Dec-16.
 */

public class RecyclerViewListTaxiAdapter extends RecyclerView.Adapter<RecyclerViewListTaxiAdapter.MyViewHolder> {

    private Context context;
    private List<TaxiType> taxiTypeList;

    public RecyclerViewListTaxiAdapter(List<TaxiType> taxiTypeList) {
        this.taxiTypeList = taxiTypeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_rv_list_taxi, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(taxiTypeList.get(position).getName());
        Picasso.with(context).load(taxiTypeList.get(position).getPhotoUrl()).resize(100, 100).centerInside().into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return taxiTypeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView photo;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_type_name);
            photo = (ImageView) itemView.findViewById(R.id.img_type);
        }
    }
}
