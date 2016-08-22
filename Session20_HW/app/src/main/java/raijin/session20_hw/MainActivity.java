package raijin.session20_hw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    private static final String imagePrefix = "upper_";
    private static final String soundPrefix = "sound_";

    public static List<Integer> imageResource;
    public static List<Integer> soundResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageResource = new ArrayList<>();
        soundResource = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            imageResource.add(this.getResources().getIdentifier(imagePrefix + i, "raw", getPackageName()));
            soundResource.add(this.getResources().getIdentifier(soundPrefix + i, "raw", getPackageName()));
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        myAdapter = new MyAdapter(this);
        recyclerView.setAdapter(myAdapter);
    }
}
