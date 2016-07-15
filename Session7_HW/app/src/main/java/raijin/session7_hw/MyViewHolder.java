package raijin.session7_hw;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 1918 on 15-Jul-16.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView nameTextView;

    public TextView getAgeTextView() {
        return ageTextView;
    }

    public void setAgeTextView(TextView ageTextView) {
        this.ageTextView = ageTextView;
    }

    public TextView getNameTextView() {
        return nameTextView;
    }

    public void setNameTextView(TextView nameTextView) {
        this.nameTextView = nameTextView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    private TextView ageTextView;

    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        nameTextView = (TextView) itemView.findViewById(R.id.name);
        ageTextView = (TextView) itemView.findViewById(R.id.age);
    }
}
