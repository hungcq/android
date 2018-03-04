package com.online.foodplus.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.R;
import com.online.foodplus.models.Answer;
import com.online.foodplus.models.Survey;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.SurveyView;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by thanhthang on 08/12/2016.
 */

public class SurveyActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout layoutParent;
    private ArrayList<Survey> datas;
    private Button bSend;
    private boolean isSending = false;
    private ArrayList<Integer> answers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        initToolbarSimple("Khảo sát người dùng");
        init();
        listener();
        defaultData();
    }

    private void defaultData() {
        datas = new ArrayList<>();
        answers = new ArrayList<>();
        Tool.get(getResources().getString(R.string.path_survey), null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                int length = response.length();
                ArrayList<Answer> answers;
                for (int i = 0; i < length; i++) {
                    Survey survey = new Survey();

                    try {
                        JSONObject object = response.getJSONObject(i);
                        survey.setQuestion(object.getString("question"));
                        JSONArray answer;
                        answer = object.getJSONArray("answer");
                        answers = new ArrayList<>();
                        for (int j = 0; j < answer.length(); j++)
                            answers.add(new Answer(answer.getJSONObject(j).getString("option")));
                        survey.setAnswer(answers);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    datas.add(survey);
                }
                int size = datas.size();
                SurveyView surveyView;
                for (int j = 0; j < size; j++) {
                    surveyView = new SurveyView(SurveyActivity.this);
                    surveyView.setQuestion(String.valueOf(j + 1) + ". " + datas.get(j).getQuestion());
                    surveyView.setAnswer(datas.get(j).getAnswer());
                    layoutParent.addView(surveyView);
                }
                bSend.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init() {
        layoutParent = (LinearLayout) findViewById(R.id.layoutParent);
        bSend = (Button) findViewById(R.id.bSend);
    }

    private void listener() {
        bSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSend:

                int count = layoutParent.getChildCount();   //Số lượng Câu hỏi đã thêm vào
                answers.clear();                            //Xoá rỗng các câu trả lời
                String missingAnswer = " ";                 //Xóa trắng các câu chưa trả lời

                for (int i = 0; i < count; i++) {
                    View view = layoutParent.getChildAt(i);

                    if (view instanceof SurveyView) {
                        int position = ((SurveyView) view).getSelected();   //Trả về -1 nếu bỏ trống
                        answers.add(position);

                        //Đánh dấu những câu hỏi mà người dùng chưa trả lời
                        if (position == -1)
                            missingAnswer += (String.valueOf(i + 1) + " ");
                    }
                }

                if (missingAnswer.trim().length() > 0) {
                    String msg = "Bạn vui lòng trả lời hết các câu hỏi [" + missingAnswer + "] trong bản khảo sát nhé !";
                    ToastCustom.show(getApplicationContext(), msg, ToastCustom.LENGTH_LONG, ToastCustom.WARNING);
                    return;
                }

                sendSurvey();

                break;
        }
    }

    private void sendSurvey() {
        RequestParams params = new RequestParams();
        for (int j = 0; j < answers.size(); j++)
            params.put("radio[]", answers.get(j));

        if (!isSending) {
            isSending = true;
            Tool.post(getResources().getString(R.string.path_survey_save), params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getString("status").equals("1")) {
                            ToastCustom.show(getApplicationContext(), "Gửi đi thành công. Xin cảm ơn !", ToastCustom.LENGTH_LONG, ToastCustom.INF0);
                            resetAllSelected();
                            bSend.setText("GỬI LẠI");
                        } else
                            ToastCustom.show(getApplicationContext(), "Xin lỗi, Bạn hãy thử lại vào lúc khác !", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    ToastCustom.show(getApplicationContext(), "Xin lỗi, Bạn hãy thử lại vào lúc khác !", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                }

                @Override
                public void onFinish() {
                    isSending = false;
                }
            });
        }

    }

    private void resetAllSelected() {
        int count = layoutParent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = layoutParent.getChildAt(i);
            if (view instanceof SurveyView) {
                ((SurveyView) view).resetAllSelected();

            }
        }
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }
}
