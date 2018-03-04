package com.online.foodplus.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.online.foodplus.R;

/**
 * Created by thanhthang on 14/12/2016.
 */

public class SpaceVertical extends LinearLayout {
    private LinearLayout space;

    public SpaceVertical(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_space_vertical, this);
            this.space = (LinearLayout) findViewById(R.id.space);
        }
    }

    public void setHeight(int height) {
        this.space.getLayoutParams().height = height;
    }
}
