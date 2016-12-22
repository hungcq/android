package raijin.taxi69.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import raijin.taxi69.R;

/**
 * Created by 1918 on 22-Dec-16.
 */

public class CustomInfoBox extends LinearLayout {

    private ViewGroup root;
    private ImageView pointImageView;
    private TextView titleTextView;
    private TextView addressTextView;

    public CustomInfoBox(Context context) {
        super(context);
    }

    public CustomInfoBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomInfoBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        ViewGroup viewGroup = (ViewGroup) inflate(getContext(), R.layout.custom_info_box, this);
        root = (ViewGroup) viewGroup.getChildAt(0);
        pointImageView = (ImageView) root.findViewById(R.id.img_point);
        titleTextView = (TextView) root.findViewById(R.id.tv_title);
        addressTextView = (TextView) root.findViewById(R.id.tv_address);
    }
}
