package raijin.session20_hw;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by 1918 on 22-Aug-16.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.word_image);
    }
}
