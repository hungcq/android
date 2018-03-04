package com.online.foodplus.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.utils.Tool;


/**
 * Created by thanhthang on 28/11/2016.
 */

public class ThumbFragment extends Fragment {
    private String image;
    private ImageView imgThumbItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        image = bundle.getString("image", "");
    }

    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_detail_item, container, false);
        imgThumbItem = (ImageView) view.findViewById(R.id.imgThumbItem);
        //        Picasso.with(getActivity()).load(Tool.findImageUrl(image, Constants.IMAGE_BIG))
        //                .into(imgThumbItem);
        Glide.with(getActivity()).load(Tool.findImageUrl(image, Constants.IMAGE_BIG))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgThumbItem);
        return view;
    }
}
