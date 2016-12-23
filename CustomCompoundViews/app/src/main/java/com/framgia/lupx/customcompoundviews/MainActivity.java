package com.framgia.lupx.customcompoundviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.framgia.lupx.customcompoundviews.widget.PlayerControlListener;
import com.framgia.lupx.customcompoundviews.widget.PlayerView;

public class MainActivity extends AppCompatActivity {
    private PlayerView playerView;
    private PlayerView playerView2;
    private TextView txtState;
    private PlayerControlListener listener = new PlayerControlListener() {
        @Override
        public void onPreviousClicked() {
            txtState.setText("Previous Clicked");
        }

        @Override
        public void onPlayClicked() {
            txtState.setText("Play Clicked");
        }

        @Override
        public void onPauseClicked() {
            txtState.setText("Pause Clicked");
        }

        @Override
        public void onStopClicked() {
            txtState.setText("Stop Clicked");
        }

        @Override
        public void onNextClicked() {
            txtState.setText("Next Clicked");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = (PlayerView) findViewById(R.id.player);
        txtState = (TextView) findViewById(R.id.state);
        playerView.setPlayerControlListener(listener);
        playerView2 = (PlayerView) findViewById(R.id.player2);
        playerView2.setPlayerControlListener(listener);
    }
}
