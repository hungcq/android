package com.online.foodplus.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.online.foodplus.R;
import com.online.foodplus.models.Answer;
import com.online.foodplus.models.Survey;
import com.online.foodplus.utils.Tool;

import java.util.ArrayList;

/**
 * - ISSUE 1:
 * How to get the selected index of a RadioGroup in Android
 * http://stackoverflow.com/questions/6440259/how-to-get-the-selected-index-of-a-radiogroup-in-android
 */

public class SurveyView extends LinearLayout implements View.OnClickListener {
    private TextView tvQestion;
    private RadioGroup radioGroup;
    private Context context;
    private RadioButton radioButton;

    public SurveyView(Context context) {
        super(context);
        init(context, null);
        this.context = context;
    }

    public SurveyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public void setData(Survey survey) {
        this.tvQestion.setText(survey.getQuestion());
        for (int i = 0; i < survey.getAnswer().size(); i++) {
            radioButton = new RadioButton(context);
            radioButton.setText(survey.getAnswer().get(i).getOption());
            radioGroup.addView(radioButton);
        }
    }

    public void setQuestion(String text) {
        this.tvQestion.setText(text);
    }

    public void setAnswer(String text) {
        radioButton = new RadioButton(context);
        radioButton.setText(text);
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen._10sdp));
        radioGroup.addView(radioButton);
    }

    public void setAnswer(ArrayList<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            radioButton = new RadioButton(context);
            radioButton.setText(answers.get(i).getOption());
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen._12sdp));
            radioGroup.addView(radioButton);
        }
    }

    public int getSelected() {
        //Xem issue 1
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        return radioGroup.indexOfChild(radioButton);
    }

    public void resetAllSelected() {
        radioGroup.clearCheck();

    }

    public void setEnable(boolean status) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(status);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.custom_surveyview, this);
            //đọc tập tin xml
            this.tvQestion = (TextView) findViewById(R.id.tvQestion);
            this.radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

            this.tvQestion.setOnClickListener(this);

            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomSurvey, 0, 0);
                try {
                    //Lấy trên XML
                    String textTitle = ta.getString(R.styleable.CustomSurvey_title);
                    //Thiết lập cho TextView trên CustomView hiện tại
                    this.tvQestion.setText(textTitle);

                } finally {
                    ta.recycle();
                }
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvQestion:
                radioGroup.startAnimation(AnimationUtils.loadAnimation(context, R.anim.view_show_scale_in));
                radioGroup.setVisibility((radioGroup.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
                break;
        }
    }
}
