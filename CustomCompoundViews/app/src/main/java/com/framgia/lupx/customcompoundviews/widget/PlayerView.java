package com.framgia.lupx.customcompoundviews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.framgia.lupx.customcompoundviews.R;

/**
 * Created by LUPX on 3/27/2016.
 */
public class PlayerView extends LinearLayout {
    private ViewGroup playerLayout;
    private ImageView btnPrev;
    private ImageView btnPlay;
    private ImageView btnPause;
    private ImageView btnStop;
    private ImageView btnNext;
    private PlayerControlListener playerControlListener;


    public PlayerView(Context context) {
        super(context);
        initialize();
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private OnClickListener buttonsClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int childCount = playerLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View btn = playerLayout.getChildAt(i);
                if (btn.getId() == v.getId()) {
                    btn.setSelected(true);
                } else {
                    btn.setSelected(false);
                }
            }
            if (playerControlListener == null) {
                return;
            }
            switch (v.getId()) {
                case R.id.btnPrev:
                    playerControlListener.onPreviousClicked();
                    break;
                case R.id.btnPlay:
                    playerControlListener.onPlayClicked();
                    break;
                case R.id.btnPause:
                    playerControlListener.onPauseClicked();
                    break;
                case R.id.btnStop:
                    playerControlListener.onStopClicked();
                    break;
                case R.id.btnNext:
                    playerControlListener.onNextClicked();
                    break;
            }
        }
    };

    private void initialize() {
        ViewGroup viewGroup = (ViewGroup) inflate(getContext(), R.layout.player_widget, this);
        playerLayout = (ViewGroup) viewGroup.getChildAt(0);
        btnPrev = (ImageView) playerLayout.findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(buttonsClick);
        btnPlay = (ImageView) playerLayout.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(buttonsClick);
        btnPause = (ImageView) playerLayout.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(buttonsClick);
        btnStop = (ImageView) playerLayout.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(buttonsClick);
        btnNext = (ImageView) playerLayout.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(buttonsClick);
    }

    public void setPlayerControlListener(PlayerControlListener listener) {
        this.playerControlListener = listener;
    }
}