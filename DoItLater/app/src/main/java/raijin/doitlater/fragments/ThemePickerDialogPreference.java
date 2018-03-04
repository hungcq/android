package raijin.doitlater.fragments;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import raijin.doitlater.R;

/**
 * Created by 1918 on 17-Sep-16.
 */
public class ThemePickerDialogPreference extends DialogPreference implements View.OnClickListener {

    private ImageView blueTheme;
    private ImageView tealTheme;
    private ImageView purpleTheme;
    private ImageView greyTheme;
    private ImageView orangeTheme;
    private ImageView pinkTheme;
    private int index;

    public ThemePickerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.theme_picker_dialog);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        initializeView(view);
        setCheckedTheme();
    }

    private void initializeView(View view) {
        blueTheme = (ImageView) view.findViewById(R.id.theme_picker_blue);
        tealTheme = (ImageView) view.findViewById(R.id.theme_picker_teal);
        purpleTheme = (ImageView) view.findViewById(R.id.theme_picker_purple);
        greyTheme = (ImageView) view.findViewById(R.id.theme_picker_grey);
        orangeTheme = (ImageView) view.findViewById(R.id.theme_picker_orange);
        pinkTheme = (ImageView) view.findViewById(R.id.theme_picker_pink);
        blueTheme.setOnClickListener(this);
        tealTheme.setOnClickListener(this);
        greyTheme.setOnClickListener(this);
        pinkTheme.setOnClickListener(this);
        orangeTheme.setOnClickListener(this);
        purpleTheme.setOnClickListener(this);
    }

    private void setCheckedTheme() {
        String currentTheme = getPersistedString("theme_default");
        if (currentTheme.equals("theme_default")) {
            index = 0;
            blueTheme.setImageResource(R.drawable.circle_blue_checked);
        } else if (currentTheme.equals("theme_purple")) {
            index = 1;
            purpleTheme.setImageResource(R.drawable.circle_purple_checked);
        } else if (currentTheme.equals("theme_teal")) {
            index = 2;
            tealTheme.setImageResource(R.drawable.circle_teal_checked);
        } else if (currentTheme.equals("theme_grey")) {
            index = 3;
            greyTheme.setImageResource(R.drawable.circle_grey_checked);
        } else if (currentTheme.equals("theme_orange")) {
            index = 4;
            orangeTheme.setImageResource(R.drawable.circle_orange_checked);
        } else if (currentTheme.equals("theme_pink")) {
            index = 5;
            pinkTheme.setImageResource(R.drawable.circle_pink_checked);
        }
    }

    private void setToDefaultImage() {
        blueTheme.setImageResource(R.drawable.circle_blue);
        purpleTheme.setImageResource(R.drawable.circle_purple);
        tealTheme.setImageResource(R.drawable.circle_teal);
        greyTheme.setImageResource(R.drawable.circle_grey);
        orangeTheme.setImageResource(R.drawable.circle_orange);
        pinkTheme.setImageResource(R.drawable.circle_pink);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.theme_picker_blue:
                setToDefaultImage();
                blueTheme.setImageResource(R.drawable.circle_blue_checked);
                index = 0;
                break;
            case R.id.theme_picker_purple:
                setToDefaultImage();
                purpleTheme.setImageResource(R.drawable.circle_purple_checked);
                index = 1;
                break;
            case R.id.theme_picker_teal:
                setToDefaultImage();
                tealTheme.setImageResource(R.drawable.circle_teal_checked);
                index = 2;
                break;
            case R.id.theme_picker_grey:
                setToDefaultImage();
                greyTheme.setImageResource(R.drawable.circle_grey_checked);
                index = 3;
                break;
            case R.id.theme_picker_orange:
                setToDefaultImage();
                orangeTheme.setImageResource(R.drawable.circle_orange_checked);
                index = 4;
                break;
            case R.id.theme_picker_pink:
                setToDefaultImage();
                pinkTheme.setImageResource(R.drawable.circle_pink_checked);
                index = 5;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            switch (index) {
                case 0:
                    persistString("theme_default");
                    break;
                case 1:
                    persistString("theme_purple");
                    break;
                case 2:
                    persistString("theme_teal");
                    break;
                case 3:
                    persistString("theme_grey");
                    break;
                case 4:
                    persistString("theme_orange");
                    break;
                case 5:
                    persistString("theme_pink");
                    break;
            }
        }
        super.onDialogClosed(positiveResult);
    }
}
