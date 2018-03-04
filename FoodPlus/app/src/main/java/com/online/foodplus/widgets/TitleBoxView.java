package com.online.foodplus.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.online.foodplus.R;
import com.squareup.picasso.Picasso;

/**
 * Created by thanhthang on 06/12/2016.
 */

public class TitleBoxView extends LinearLayout {
    private ImageView imgIcon;
    private TextView tvTitle;
    private Context context;

    public TitleBoxView(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public void setData(String title, String icon) {
        this.tvTitle.setText(title);
        Picasso.with(context)
                .load(icon)
                .resize(90, 90)
                .centerCrop().into(this.imgIcon);

    }

    public TitleBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        //chuẩn bị đọc tập tin custom_button.xml
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_titlebox, this);

            //đọc tập tin custom_button.xml lấy về 2 View tương ứng
            this.imgIcon = (ImageView) findViewById(R.id.imgIcon);
            this.tvTitle = (TextView) findViewById(R.id.tvTitle);
            if (attrs != null) {
                //lấy thuộc tính text từ thẻ com.danweb.vietnamtourism.CustomButton
                CharSequence labelText = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");

                //thiết lập cho TextView trên CustomButton hiện tại
                this.tvTitle.setText(labelText);

                //lấy id của drawable từ thẻ com.danweb.vietnamtourism.CustomButton
                int resId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);

                //thiết lập cho ImageView trên CustomButton hiện tại
                this.imgIcon.setImageResource(resId);
            }
        }
    }

    public String getTitle() {
        return tvTitle.getText().toString();
    }
}
