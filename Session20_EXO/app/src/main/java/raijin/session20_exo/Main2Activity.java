package raijin.session20_exo;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {

    private Button playButton;
    private SoundPool soundPool;
    private ArrayList<Integer> list;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.play_button);
        random = new Random();
        list = new ArrayList<>();
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        list.add(soundPool.load(this, R.raw.sound_0, 1));
        list.add(soundPool.load(this, R.raw.sound_1, 1));
        list.add(soundPool.load(this, R.raw.sound_2, 1));
        list.add(soundPool.load(this, R.raw.sound_3, 1));

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundPool.play(list.get(random.nextInt(4)), 1.0f, 1.0f, 1, 1, 1.0f);
            }
        });
    }
}
