package com.online.foodplus.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.online.foodplus.Constants;
import com.online.foodplus.R;
import com.online.foodplus.activities.DetailActivity;
import com.online.foodplus.models.Base;
import com.online.foodplus.utils.Tool;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by thanhthang on 06/12/2016.
 */

public class GalleryView extends LinearLayout implements View.OnClickListener {
    private ImageView imgFeature, img1, img2, img3, img4;
    private TextView tvTitle, tvRating, tvDescription, tvStar, tvFavourite, tvComment, tvPin;
    private TextView tvDesc1, tvDesc2, tvDesc3, tvDesc4;
    private Context context;
    private ArrayList<Base> datas;

    public GalleryView(Context context) {
        super(context);
        init(context, null);
        this.context = context;
    }

    public void setData(ArrayList<Base> datas) {
        this.datas = datas;
        int size = datas.size();
        if (size == 0)
            return;

        tvTitle.setText(datas.get(0).getTitle());
        tvRating.setText(datas.get(0).getRating());
        String description = datas.get(0).getDescription();
        if (description != null && !description.equals("null"))
            tvDescription.setText(description);
        else
            tvDescription.setVisibility(View.GONE);
        tvStar.setText(datas.get(0).getStar());
        tvFavourite.setText(datas.get(0).getFavourite());
        tvComment.setText(datas.get(0).getComment());
        tvPin.setText(datas.get(0).getPin());


        if (size >= 1 && datas.get(0) != null)
            if (datas.get(0).getImages().get(0) != null)
                Picasso.with(context)
                        .load(datas.get(0).getImages().get(0))
                        //.resize(200, 200)
                        //.centerCrop()
                        .into(imgFeature);


        if (size >= 2 && datas.get(1) != null) {
            if (datas.get(1).getImages().get(0) != null)
                Picasso.with(context)
                        .load(Tool.findImageUrl(datas.get(1).getImages().get(0), Constants.IMAGE_SMALL))
                        .resize(200, 200)
                        .centerCrop()
                        .into(img1);
            if (datas.get(1).getDescription() != null && !datas.get(1).getDescription().equals("null")) {
                tvDesc1.setText(datas.get(1).getDescription());
                tvDesc1.setVisibility(View.VISIBLE);
            }
        }


        if (size >= 3 && datas.get(2) != null) {
            if (datas.get(2).getImages().get(0) != null)
                Picasso.with(context)
                        .load(Tool.findImageUrl(datas.get(2).getImages().get(0), Constants.IMAGE_SMALL))
                        .resize(200, 200)
                        .centerCrop()
                        .into(img2);
            if (datas.get(2).getDescription() != null && !datas.get(2).getDescription().equals("null")) {
                tvDesc2.setText(datas.get(2).getDescription());
                tvDesc2.setVisibility(View.VISIBLE);
            }
        }
        if (size >= 4 && datas.get(3) != null) {
            if (datas.get(3).getImages().get(0) != null)
                Picasso.with(context)
                        .load(Tool.findImageUrl(datas.get(3).getImages().get(0), Constants.IMAGE_SMALL))
                        //.resize(200, 200)
                        //.centerCrop()
                        .into(img3);
            if (datas.get(3).getDescription() != null && !datas.get(3).getDescription().equals("null")) {
                tvDesc3.setText(datas.get(3).getDescription());
                tvDesc3.setVisibility(View.VISIBLE);
            }
        }

        if (size >= 5 && datas.get(4) != null) {
            if (datas.get(4).getImages().get(0) != null)
                Picasso.with(context)
                        .load(Tool.findImageUrl(datas.get(4).getImages().get(0), Constants.IMAGE_SMALL))
                        //.resize(200, 200)
                        //.centerCrop()
                        .into(img4);
            if (datas.get(4).getDescription() != null && !datas.get(4).getDescription().equals("null")) {
                tvDesc4.setText(datas.get(4).getDescription());
                tvDesc4.setVisibility(View.VISIBLE);
            }
        }

    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_gallery, this);
            //đọc tập tin xml
            imgFeature = (ImageView) findViewById(R.id.imgFeature);
            tvTitle = (TextView) findViewById(R.id.tvTitle);
            tvRating = (TextView) findViewById(R.id.tvRating);
            tvDescription = (TextView) findViewById(R.id.tvDescription);
            tvStar = (TextView) findViewById(R.id.tvStar);
            tvComment = (TextView) findViewById(R.id.tvComment);
            tvPin = (TextView) findViewById(R.id.tvPin);
            tvFavourite = (TextView) findViewById(R.id.tvFavourite);
            img1 = (ImageView) findViewById(R.id.img1);
            img2 = (ImageView) findViewById(R.id.img2);
            img3 = (ImageView) findViewById(R.id.img3);
            img4 = (ImageView) findViewById(R.id.img4);
            tvDesc1 = (TextView) findViewById(R.id.tvDesc1);
            tvDesc2 = (TextView) findViewById(R.id.tvDesc2);
            tvDesc3 = (TextView) findViewById(R.id.tvDesc3);
            tvDesc4 = (TextView) findViewById(R.id.tvDesc4);

            img1.setOnClickListener(this);
            img2.setOnClickListener(this);
            img3.setOnClickListener(this);
            img4.setOnClickListener(this);
            imgFeature.setOnClickListener(this);

            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomGallery, 0, 0);
                try {
                    //Lấy trên XML
                    String textTitle = ta.getString(R.styleable.CustomGallery_title);
                    String textRating = ta.getString(R.styleable.CustomGallery_rating);
                    String textDescription = ta.getString(R.styleable.CustomGallery_description);

                    //Thiết lập cho TextView trên CustomView hiện tại
                    tvTitle.setText(textTitle);
                    tvRating.setText(textRating);
                    tvDescription.setText(textDescription);
                    //lấy id của drawable từ xml
                    int resId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
                    //thiết lập cho ImageView trên CustomButton hiện tại
                    imgFeature.setImageResource(resId);
                } finally {
                    ta.recycle();
                }
            }

        }
    }

    @Override
    public void onClick(View view) {
        String id = null;
        String cid = null;
        String t = null;
        int size = datas.size();
        switch (view.getId()) {
            case R.id.imgFeature:
                if (size >= 1 && datas.get(0) != null) {
                    id = datas.get(0) != null ? datas.get(0).getId() : null;
                    cid = datas.get(0) != null ? datas.get(0).getCid() : null;
                    t = datas.get(0) != null ? datas.get(0).getTid() : null;
                }
                break;
            case R.id.img1:
                if (size >= 2 && datas.get(1) != null) {
                    id = datas.get(1) != null ? datas.get(1).getId() : null;
                    cid = datas.get(1) != null ? datas.get(1).getCid() : null;
                    t = datas.get(1) != null ? datas.get(1).getTid() : null;
                }
                break;
            case R.id.img2:
                if (size >= 3 && datas.get(2) != null) {
                    id = datas.get(2) != null ? datas.get(2).getId() : null;
                    cid = datas.get(2) != null ? datas.get(2).getCid() : null;
                    t = datas.get(2) != null ? datas.get(2).getTid() : null;
                }
                break;
            case R.id.img3:
                if (size >= 4 && datas.get(3) != null) {
                    id = datas.get(3) != null ? datas.get(3).getId() : null;
                    cid = datas.get(3) != null ? datas.get(3).getCid() : null;
                    t = datas.get(3) != null ? datas.get(3).getTid() : null;
                }
                break;
            case R.id.img4:
                if (size >= 5 && datas.get(4) != null) {
                    id = datas.get(4) != null ? datas.get(4).getId() : null;
                    cid = datas.get(4) != null ? datas.get(4).getCid() : null;
                    t = datas.get(4) != null ? datas.get(4).getTid() : null;
                }

                break;
        }
        if (id != null) {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("t", t);
            intent.putExtra("cid", cid);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //prevent Exception crash when startActivity from custom view
            context.startActivity(intent);

            //Animation if Activity
            if (context instanceof Activity)
                ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }


}
