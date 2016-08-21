package raijin.session20_exo;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.Util;

public class MainActivity extends AppCompatActivity {

    private static String URL = "http://s82.stream.nixcdn.com/b52c16377106ce46a21df208a0814da1/57b975a9/NhacCuaTui908/Liberators-EpicScore-4144692.mp3";
    private static int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static int BUFFER_SEGMENT_COUNT = 256;

    private ExoPlayer exoPlayer;
    private MediaCodecAudioTrackRenderer mediaCodecAudioTrackRenderer;

    //
    private TextView titleTextView;
    private Button playButton;

    private boolean isPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize exo
        exoPlayer = ExoPlayer.Factory.newInstance(BIND_AUTO_CREATE);

        //
        titleTextView = (TextView) findViewById(R.id.title_text);
        playButton = (Button) findViewById(R.id.play_button);

        isPlay = false;

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlay) {
                    exoPlayer.setPlayWhenReady(true);
                    playButton.setText("Pause");
                    isPlay = true;
                } else {
                    exoPlayer.setPlayWhenReady(false);
                    playButton.setText("Play");
                    isPlay = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStreamMusic(URL);
    }

    private void loadStreamMusic(String url) {
        //get title
        String title = url.substring(url.lastIndexOf("/") + 1);
        titleTextView.setText(title);
        Uri uri = Uri.parse(url);
        Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);

        String userAgent = Util.getUserAgent(this, "Session20_EXO");

        DataSource dataSource = new DefaultUriDataSource(this, null, userAgent);

        ExtractorSampleSource sampleSource = new ExtractorSampleSource(
                uri, dataSource, allocator, BUFFER_SEGMENT_SIZE * BUFFER_SEGMENT_COUNT);

        mediaCodecAudioTrackRenderer = new MediaCodecAudioTrackRenderer(sampleSource);
        // Prepare ExoPlayer
        exoPlayer.prepare(mediaCodecAudioTrackRenderer);
    }
}
