package raijin.session19_mediaplayer;

import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView titleTextView;
    private TextView timeTextView;
    private SeekBar seekBar;
    private Button playButton;
    private Button pauseButton;
    private Button resetButton;

    //
    private MediaPlayer mediaPlayer;
    private Handler handler;

    //
    private boolean isReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        addListener();
    }

    private void initialize() {
        titleTextView = (TextView) findViewById(R.id.title_text);
        timeTextView = (TextView) findViewById(R.id.time_text);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        playButton = (Button) findViewById(R.id.play_button);
        pauseButton = (Button) findViewById(R.id.pause_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        //
        handler = new Handler();

        //
        isReset = true;
        titleTextView.setText(getTitles());
    }

    private void addListener() {
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                int currentTime = mediaPlayer.getCurrentPosition();
                timeTextView.setText(String.format("%02d:%02d",currentTime/60000,(currentTime/1000)%60));
            }
            handler.postDelayed(this, 100);
        }
    };

    public void updateUI() {
        MainActivity.this.runOnUiThread(updateTime);
    }

    private String getTitles() {
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.herowithin_countdown);
        MediaMetadataRetriever m = new MediaMetadataRetriever();
        m.setDataSource(this,uri);
        return m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_button:
                if (isReset) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.herowithin_countdown);
                    isReset = false;
                }

                if (!mediaPlayer.isPlaying()) {
                    playButton.setText("Play");
                }
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                updateUI();
                //handler.postDelayed(updateTime, 1000);
                break;
            case R.id.pause_button:
                if (mediaPlayer.isPlaying()) {
                    playButton.setText("Resume");
                    mediaPlayer.pause();
                }
                break;
            case R.id.reset_button:
                if (mediaPlayer != null) {
                    isReset = true;
                    mediaPlayer.reset();
                    seekBar.setProgress(0);
                    handler.removeCallbacks(updateTime);
                }
                break;
            default:
                break;
        }
    }
}
