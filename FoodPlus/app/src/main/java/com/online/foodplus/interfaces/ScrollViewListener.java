package com.online.foodplus.interfaces;

import com.online.foodplus.widgets.ObservableScrollView;

/**
 * Created by thanhthang on 24/12/2016.
 */

public interface ScrollViewListener {
    void onScrollEnded(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
