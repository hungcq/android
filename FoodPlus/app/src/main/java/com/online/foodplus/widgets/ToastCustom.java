package com.online.foodplus.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.online.foodplus.R;

public class ToastCustom {
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_SHORT = 0;
    public static final int SUCCESS = 1;
    public static final int INF0 = 3;
    public static final int WARNING = 2;
    public static final int ERROR = 0;


    public static void show(Context context, String msg, int duration, int type) {
        createToast(context, msg, duration, type);
    }

    public static void show(Context context, String msg, int duration) {
        createToast(context, msg, duration, SUCCESS);
    }

    private static void createToast(Context context, String msg, int duration, int type) {

        int bg_color = ContextCompat.getColor(context, R.color.Cyan50);
        int text_color = ContextCompat.getColor(context, R.color.Blue900);
        int icon = R.drawable.ic_info_blue;

        switch (type) {
            case WARNING:
                icon = R.drawable.ic_warning_amber;
                bg_color = ContextCompat.getColor(context, R.color.Amber100);
                text_color = ContextCompat.getColor(context, R.color.Orange);
                break;

            case ERROR:
                icon = R.drawable.ic_error_red;
                bg_color = ContextCompat.getColor(context, R.color.Red100);
                text_color = ContextCompat.getColor(context, R.color.Red700);
                break;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.custom_toast, null);
        ((TextView) layout.findViewById(R.id.text)).setText(msg);
        (layout.findViewById(R.id.toast_layout_root)).setBackgroundColor(bg_color);
        ((ImageView) layout.findViewById(R.id.thumb)).setImageResource(icon);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);
        text.setTextColor(text_color);

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }


}
