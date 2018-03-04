package com.online.foodplus.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.online.foodplus.R;

/**
 * Created by thanhthang on 14/12/2016.
 */

public class HolderEmptyView extends LinearLayout {
    private LinearLayout holderParent;

    public HolderEmptyView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.holder_empty, this);
            holderParent = (LinearLayout) findViewById(R.id.holderParent);
        }
    }

    public void addView(View view) {
        holderParent.addView(view);
    }
}
