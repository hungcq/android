package com.online.foodplus.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.online.foodplus.R;

/**
 * Created by 1918 on 22-Nov-16.
 */

public class MySliderView extends BaseSliderView {

    private ImageInfo imageInfo;

    public MySliderView (Context context, ImageInfo imageInfo) {
        super(context);
        this.imageInfo = imageInfo;
    }
    @Override
    public View getView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_slider_layout, null);
        ImageView image = (ImageView) view.findViewById(R.id.slider_image);
        TextView text = (TextView) view.findViewById(R.id.slider_text);
        text.setText(imageInfo.getDescription());
        bindEventAndShow(view, image);
        return view;
    }
}
