package com.online.foodplus.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.online.foodplus.R;

/**
 * Created by 1918 on 28-Dec-16.
 */

public class InfoText extends RelativeLayout {
    private RelativeLayout parent;
    private EditText editText;
    private TextView textView;
    private boolean readOnly = false;

    public InfoText(Context context) {
        super(context);
        init();
    }

    public InfoText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfoText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewGroup viewGroup = (ViewGroup) inflate(getContext(), R.layout.custom_info_text, this);
        ViewGroup root = (ViewGroup) viewGroup.getChildAt(0);
        parent = (RelativeLayout) root.findViewById(R.id.parent);
        editText = (EditText) root.findViewById(R.id.et);
        textView = (TextView) root.findViewById(R.id.tv);
    }

    public void setText(String string) {
        editText.setText(string);
    }

    public void setHint(String string) {
        editText.setHint(string);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setFieldName(String fieldName) {
        textView.setText(fieldName);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setEditable(boolean editable) {

        if (editable) {
            if (readOnly)
                parent.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.Grey100));
            else
                editText.setEnabled(true);

            editText.setTextColor(ContextCompat.getColor(editText.getContext(), R.color.black));
        } else {
            if (readOnly)
                parent.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.white));
            editText.setEnabled(false);
            editText.setTextColor(ContextCompat.getColor(editText.getContext(), R.color.disable));
        }
    }
}
