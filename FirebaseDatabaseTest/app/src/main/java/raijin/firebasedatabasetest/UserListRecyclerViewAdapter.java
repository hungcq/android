package raijin.firebasedatabasetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 1918 on 21-Dec-16.
 */

public class UserListRecyclerViewAdapter extends RecyclerView.Adapter<UserListRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<UserInfo> userInfoList;

    public UserListRecyclerViewAdapter(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_user_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(userInfoList.get(position).getName());
        holder.phoneNumber.setText(userInfoList.get(position).getPhoneNumber());
        if (userInfoList.get(position).getAvatarUrl() != null && !userInfoList.get(position).getAvatarUrl().equals("")) {
            Picasso.with(context).load(userInfoList.get(position).getAvatarUrl()).resize(100,100).centerCrop().into(holder.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name;
        TextView phoneNumber;

        public MyViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.item_avatar);
            name = (TextView) itemView.findViewById(R.id.item_name);
            phoneNumber = (TextView) itemView.findViewById(R.id.item_number);
        }
    }
}
