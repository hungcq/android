package com.online.foodplus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.online.foodplus.R;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.online.foodplus.utils.Tool.post;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private Button btnRegisterAction;
    private EditText etUsername, etPassword, etRePassword, etEmail;
    private RadioGroup radioGroup;
    private RadioButton radioFemale, radioMale;
    private RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        initToolbarSimple("Đăng ký", false);
        click();
        received();
    }


    private void init() {
        btnRegisterAction = (Button) findViewById(R.id.btnRegisterAction);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
    }

    /**
     * Sử dụng khi nhận dữ liệu login của Facebook hoặc Google
     * Set giá trị mặc định cho các trường sẵn có
     **/
    private void received() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra("username")) {
                String username = extras.getString("username");
                etUsername.setText(username);
            }
            if (getIntent().hasExtra("email")) {
                String email = extras.getString("email");
                etEmail.setText(email);
            }
            if (getIntent().hasExtra("gender")) {
                int gender = extras.getInt("gender", 1);
                if (gender == 1)
                    radioMale.setChecked(true);
                else
                    radioFemale.setChecked(true);
            }

        }

    }

    private void click() {
        btnRegisterAction.setOnClickListener(this);
    }

    private void action() {
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        String radioSelected = radioButton.getText().toString();
        int gender = radioSelected.equalsIgnoreCase("Male") ? 0 : 1;
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String rePassword = etRePassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        //Xác thực Input phía client
        if (username.length() == 0 || password.length() == 0 || rePassword.length() == 0 || email.length() == 0) {
            ToastCustom.show(getApplicationContext(), "Vui lòng điền đầy đủ vào các trường", ToastCustom.LENGTH_LONG, ToastCustom.WARNING);
            return;
        }
        if (!password.contentEquals(rePassword)) {
            ToastCustom.show(getApplicationContext(), "Mật khẩu không khớp nhau", ToastCustom.LENGTH_LONG, ToastCustom.WARNING);
            return;
        }

        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            ToastCustom.show(getApplicationContext(), "Email sai định dạng", ToastCustom.LENGTH_LONG, ToastCustom.WARNING);
            return;
        }

        //Kết nối server đăng ky
        runRegister(username, password, email, gender);
    }

    private void runRegister(final String username, final String password, final String email, final int gender) {
        params = new RequestParams();
        params.put("t", "token");
        Tool.post(getResources().getString(R.string.api_user_srv), params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String tokenUser = response.has("token_user") ? response.getString("token_user") : null;
                    String tokenPassword = response.has("token_password") ? response.getString("token_password") : null;
                    params = new RequestParams();
                    params.put("t", "reg");
                    params.put("usr", username);
                    params.put("pwd", password);
                    params.put("email", email);
                    params.put("gender", gender);
                    params.put("au", Tool.getHashToken(tokenUser + tokenPassword));

                    post(getResources().getString(R.string.api_user_srv), params, new JsonHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            try {
                                //Kiểm tra trạng thái Đăng ký (1:thành công)

                                int status = response.getInt("status");
                                if (status == 1) {
                                    etUsername.setText("");
                                    etEmail.setText("");
                                    etPassword.setText("");
                                    etRePassword.setText("");
                                    //Chuyển trang
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("password", password);
                                    startActivity(intent);
                                    finish();
                                } else if (status == 0) {
                                    //Gửi thông điệp báo lỗi
                                    String message = response.getString("message");
                                    showToast(message);
                                } else
                                    ToastCustom.show(getApplicationContext(), "Lỗi, xin hãy thử lại vào lúc khác ", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                ToastCustom.show(getApplicationContext(), "Lỗi, xin hãy thử lại vào lúc khác ", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            ToastCustom.show(getApplicationContext(), "Lỗi: "+String.valueOf(statusCode), ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                        }

                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToastCustom.show(getApplicationContext(), "Token Failed", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegisterAction:
                action();
                break;
        }
    }
}
