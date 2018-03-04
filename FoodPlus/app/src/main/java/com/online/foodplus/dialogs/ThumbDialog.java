package com.online.foodplus.dialogs;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.foodplus.R;
import com.online.foodplus.adapters.ThumbAdapter;

import java.util.ArrayList;

/**
 * Created by thanhthang on 28/11/2016.
 */

public class ThumbDialog extends DialogFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static ArrayList<String> listItems;
    private static int currentPosition;
    private TextView tvCounter;
    private static int total = 1;
    private ViewPager viewPager;
    private ImageView imgNext, imgPrev,imgClose;

    public ThumbDialog() {
        super();
    }


    public static ThumbDialog newInstance(ArrayList<String> listItem, int curPos) {
        ThumbDialog dialog = new ThumbDialog();
        listItems = listItem;
        total = listItem.size();
        currentPosition = curPos;
        return dialog;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window != null) {
            dialog.setCanceledOnTouchOutside(true);
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, screenWidth);//dialog height = screen width
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_thumb, container);
        init(view);
        listener(view);
        defaultData();
        controlUpdateVisibility();
        return view;
    }

    private void init(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        tvCounter = (TextView) view.findViewById(R.id.tvCounter);
        imgClose = (ImageView) view.findViewById(R.id.imgClose);
        imgNext = (ImageView) view.findViewById(R.id.imgNext);
        imgPrev = (ImageView) view.findViewById(R.id.imgPrev);
    }

    private void listener(View v) {
        imgClose.setOnClickListener(this);
        imgPrev.setOnClickListener(this);
        imgNext.setOnClickListener(this);
    }

    private void defaultData() {
        viewPager.setAdapter(new ThumbAdapter(getChildFragmentManager(), listItems));
        viewPager.setCurrentItem(currentPosition);
        viewPager.addOnPageChangeListener(this);
        tvCounter.setText(String.valueOf(currentPosition + 1) + "/" + String.valueOf(total));
    }

    private void controlUpdateVisibility() {
        imgPrev.setVisibility((currentPosition - 1 == 0) ? View.VISIBLE : View.GONE);
        imgNext.setVisibility((currentPosition + 1 < total) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        tvCounter.setText(String.valueOf(position + 1) + "/" + String.valueOf(total));
        controlUpdateVisibility();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgPrev:
                if (currentPosition - 1 == 0)
                    viewPager.setCurrentItem(currentPosition - 1, true);
                break;
            case R.id.imgNext:
                if (currentPosition + 1 < total)
                    viewPager.setCurrentItem(currentPosition + 1, true);

                break;
            case R.id.imgClose:
                dismiss();
                break;
        }
        controlUpdateVisibility();
    }
}
