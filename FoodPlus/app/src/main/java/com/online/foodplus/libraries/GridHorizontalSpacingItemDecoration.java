package com.online.foodplus.libraries;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by thanhthang on 29/12/2016.
 */

public class GridHorizontalSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public GridHorizontalSpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = spacing / 2;
        outRect.left = spacing / 2;
        outRect.right = spacing / 2;
        outRect.bottom = spacing / 2; // item bottom
    }
}
