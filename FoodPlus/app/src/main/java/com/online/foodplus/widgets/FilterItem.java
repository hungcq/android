package com.online.foodplus.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.online.foodplus.R;

/**
 * Created by 1918 on 12-Jan-17.
 */

public class FilterItem extends LinearLayout {

    private ImageView icon;
    private TextView text;
    private ViewGroup root;

    public FilterItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FilterItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        ViewGroup viewGroup = (ViewGroup) inflate(getContext(), R.layout.custom_filter_item, this);
        root = (ViewGroup) viewGroup.getChildAt(0);
        icon = (ImageView) root.findViewById(R.id.item_icon);
        text = (TextView) root.findViewById(R.id.item_text);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.FilterItem,
                0, 0);

        try {
            icon.setImageResource(a.getResourceId(R.styleable.FilterItem_icon, 0));
            text.setText(a.getString(R.styleable.FilterItem_content));
        } finally {
            a.recycle();
        }

    }

    public void setData(int imageResource, String fieldName) {
        icon.setImageResource(imageResource);
        text.setText(fieldName);
    }

    public void setFieldContent(String content) {
        text.setText(content);
    }
}
