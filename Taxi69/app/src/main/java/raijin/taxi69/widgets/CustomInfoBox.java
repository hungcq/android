package raijin.taxi69.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import raijin.taxi69.R;

/**
 * Created by 1918 on 22-Dec-16.
 */

public class CustomInfoBox extends LinearLayout implements View.OnClickListener {

    private ViewGroup root;
    private ImageView pointImageView;
    private TextView titleTextView;
    private TextView addressTextView;

    public CustomInfoBox(Context context) {
        super(context);
        init();
    }

    public CustomInfoBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomInfoBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewGroup viewGroup = (ViewGroup) inflate(getContext(), R.layout.custom_info_box, this);
        root = (ViewGroup) viewGroup.getChildAt(0);
        pointImageView = (ImageView) root.findViewById(R.id.img_point);
        titleTextView = (TextView) root.findViewById(R.id.tv_title);
        addressTextView = (TextView) root.findViewById(R.id.tv_address);

        root.setOnClickListener(this);
    }

    public void initData(int imageResource, String title, String address) {
        pointImageView.setImageResource(imageResource);
        titleTextView.setText(title);
        addressTextView.setText(address);
    }

    public void setAddress(String address) {
        addressTextView.setText(address);
    }

    @Override
    public void onClick(View view) {

    }
}
