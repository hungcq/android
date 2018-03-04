package com.online.foodplus.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.R;
import com.online.foodplus.activities.LoginActivity;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.InfoText;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by thanhthang on 25/11/2016.
 */

public class UserInfoFragment extends Fragment implements View.OnClickListener {

    private InfoText usernameInfoText;
    private InfoText nameInfoText;
    private InfoText passwordInfoText;
    private InfoText numberInfoText;
    private InfoText emailInfoText;
    private Button editButton;
    private RadioGroup radioGroup;
    private RadioButton maleButton;
    private RadioButton femaleButton;
    private boolean editing = false;
    private View view;
    private boolean isSending = false;
    private String uid, email, gender, name, phone, usr, token;
    private SharedPreferences pref;
    private String inputName, inputPhone, inputGender;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user_info, container, false);
        init(view);
        getData();
        return view;
    }

    private void init(final View view) {
        usernameInfoText = (InfoText) view.findViewById(R.id.info_text_username);
        nameInfoText = (InfoText) view.findViewById(R.id.info_text_name);
        passwordInfoText = (InfoText) view.findViewById(R.id.info_text_password);
        numberInfoText = (InfoText) view.findViewById(R.id.info_text_number);
        emailInfoText = (InfoText) view.findViewById(R.id.info_text_email);

        radioGroup = (RadioGroup) view.findViewById(R.id.rgGender);
        maleButton = (RadioButton) view.findViewById(R.id.radioMale);
        femaleButton = (RadioButton) view.findViewById(R.id.radioFemale);

        usernameInfoText.setFieldName("Tài khoản:");
        usernameInfoText.setReadOnly(true);
        nameInfoText.setFieldName(getResources().getString(R.string.ten_hien_thi));
        passwordInfoText.setFieldName(getResources().getString(R.string.mat_khau));
        passwordInfoText.setHint("********");
        passwordInfoText.setReadOnly(true);
        emailInfoText.setFieldName(getResources().getString(R.string.email));
        emailInfoText.setReadOnly(true);
        numberInfoText.setFieldName(getResources().getString(R.string.so_dien_thoai));

        editButton = (Button) view.findViewById(R.id.btn);
        editButton.setOnClickListener(this);
    }

    private void getData() {
        pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        uid = pref.getString("user_id", "");
        usr = pref.getString("username", "");
        gender = pref.getString("gender", "0");
        name = pref.getString("name", "0");
        phone = pref.getString("phone", "");
        email = pref.getString("email", "");
        nameInfoText.setText(name);
        numberInfoText.setText(phone);

        if (!isSending && !uid.equals("") && !usr.equals("") && !email.equals("")) {
            if (!usr.equals("null"))
                usernameInfoText.setText(usr);
            emailInfoText.setText(email);

            if (gender.equals("1")) {
                maleButton.setChecked(true);
                femaleButton.setChecked(false);
            } else {
                maleButton.setChecked(false);
                femaleButton.setChecked(true);
            }
        } else {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }

        if (!isSending) {
            isSending = true;
            RequestParams params = new RequestParams();
            params.put("t", "token");
            Tool.get(getResources().getString(R.string.api_user_srv), params, new JsonHttpResponseHandler() {
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    ToastCustom.show(getActivity(), "Xin hãy thử lại vào lúc khác", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    ToastCustom.show(getActivity(), "Xin hãy thử lại vào lúc khác", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }

                @Override
                public void onFinish() {
                    isSending = false;
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String tokenUser = Tool.getStringJson("token_user", response);
                    String tokenPassword = Tool.getStringJson("token_password", response);
                    if (tokenUser != null && tokenPassword != null)
                        token = Tool.getHashToken(tokenUser + tokenPassword);
                    else
                        ToastCustom.show(getActivity(), "Xin hãy thử lại vào lúc khác", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                }
            });
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn:
                if (editing) {
                    if (!isSending) {
                        isSending = true;
                        editButton.setText(R.string.chinh_sua);
                        // save data
                        if (token != null) {
                            inputName = nameInfoText.getText().trim();
                            inputPhone = numberInfoText.getText().trim();
                            RequestParams params = new RequestParams();
                            params.put("t", "upd");
                            params.put("uid", uid);
                            params.put("usr", usr);
                            params.put("email", email);
                            params.put("pwd", "a");
                            params.put("au", token);
                            params.put("mob", inputPhone);
                            params.put("fname", inputName);

                            int selectedId = radioGroup.getCheckedRadioButtonId();

                            if (selectedId != -1) {
                                RadioButton radioButton = (RadioButton) view.findViewById(selectedId);
                                String radioSelected = radioButton.getText().toString();
                                int genderValue = radioSelected.equalsIgnoreCase("Nam") ? 1 : 0;
                                params.put("gender", genderValue);
                                inputGender = String.valueOf(genderValue);
                            }


                            Tool.post(getResources().getString(R.string.api_user_srv), params, new JsonHttpResponseHandler() {
                                @Override
                                public void onFinish() {
                                    isSending = false;
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    if (Tool.getIntJson("status", response) == 1) {
                                        if (inputGender != null)
                                            pref.edit().putString("gender", inputGender).apply();
                                        pref.edit().putString("name", inputName).apply();
                                        pref.edit().putString("phone", inputPhone).apply();

                                        usernameInfoText.setEditable(false);
                                        nameInfoText.setEditable(false);
                                        passwordInfoText.setEditable(false);
                                        emailInfoText.setEditable(false);
                                        numberInfoText.setEditable(false);
                                        maleButton.setEnabled(false);
                                        femaleButton.setEnabled(false);
                                        editing = false;
                                    } else {
                                        String msg = Tool.getStringJson("message", response);
                                        ToastCustom.show(getActivity(), msg != null ? msg : "Xin hãy thử lại vào lúc khác", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
                                    }
                                }
                            });
                        }
                    }
                } else {
                    editButton.setText(R.string.luu);
                    usernameInfoText.setEditable(true);
                    nameInfoText.setEditable(true);
                    passwordInfoText.setEditable(true);
                    emailInfoText.setEditable(true);
                    numberInfoText.setEditable(true);
                    maleButton.setEnabled(true);
                    femaleButton.setEnabled(true);
                    editing = true;
                }
                break;
        }
    }
}
