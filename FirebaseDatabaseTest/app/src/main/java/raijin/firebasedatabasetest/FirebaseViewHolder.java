package raijin.firebasedatabasetest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 1918 on 22-Dec-16.
 */

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    public ImageView avatar;
    public TextView name;
    public TextView phoneNumber;

    public FirebaseViewHolder(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.item_avatar);
        name = (TextView) itemView.findViewById(R.id.item_name);
        phoneNumber = (TextView) itemView.findViewById(R.id.item_number);
    }
}
