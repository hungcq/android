package raijin.session20_hw;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1918 on 22-Aug-16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private SoundPool soundPool;

    public MyAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_item, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        final List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            list.add(soundPool.load(context, MainActivity.soundResource.get(i), 1));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                soundPool.play(list.get(position), 1.0f, 1.0f, 1, 1, 1.0f);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.getImageView().setImageResource(MainActivity.imageResource.get(position));
    }

    @Override
    public int getItemCount() {
        return MainActivity.imageResource.size();
    }
}
