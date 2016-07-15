package raijin.session7_hw;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 1918 on 15-Jul-16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context mContext;
    private List<PersonInfo> listEmployee;

    public MyAdapter(List<PersonInfo> listEmployee, Context context) {
        super();
        this.listEmployee = listEmployee;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.getImageView().setImageResource(R.mipmap.drow);
        holder.getNameTextView().setText("Name: " + listEmployee.get(position).getName());
        holder.getAgeTextView().setText("Age: " + listEmployee.get(position).getAge());
    }

    @Override
    public int getItemCount() {
        return listEmployee.size();
    }
}
