package com.online.foodplus.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.online.foodplus.R;
import com.online.foodplus.utils.Md5;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.widgets.ToastCustom;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.online.foodplus.utils.Tool.post;

/**
 * Created by thanhthang on 22/11/2016.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Button btnLoginNormal;
    //private CallbackManager callbackManager;
    private Button btnRegister;
    private EditText etUsername, etPassword;
    private SharedPreferences pref;
    private CircularImageView btnFacebookLogin, btnGooglePlusLogin;
    private ImageView imageBack;
    private String token;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FacebookSdk.sdkInitialize(getApplicationContext());
        // callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        init();
        initToolbarSimple("Đăng nhập", false);
        click();
        value();
        getToken();
        received();
    }

    private void received() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (getIntent().hasExtra("username")) {
                String username = extras.getString("username");
                etUsername.setText(username);
            }
            if (getIntent().hasExtra("password")) {
                String password = extras.getString("password");
                etPassword.setText(password);
            }
            //actionLoginNormal();

        }
    }

    private void value() {
        pref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    private void getToken() {
        RequestParams params = new RequestParams();
        params.put("t", "token");
        post(getResources().getString(R.string.api_user_srv), params, new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ToastCustom.show(getApplicationContext(), "Xin hãy thử lại vào lúc khác", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToastCustom.show(getApplicationContext(), "Xin hãy thử lại vào lúc khác", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
            }

            @Override
            public void onFinish() {
                isLoading = false;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String tokenUser = Tool.getStringJson("token_user", response);
                String tokenPassword = Tool.getStringJson("token_password", response);
                if (tokenUser != null && tokenPassword != null)
                    token = Tool.getHashToken(tokenUser + tokenPassword);
                else
                    ToastCustom.show(getApplicationContext(), "Xin hãy thử lại vào lúc khác", ToastCustom.LENGTH_SHORT, ToastCustom.ERROR);
            }
        });
    }

    private void init() {
        imageBack = (ImageView) findViewById(R.id.imgToolbarBack);
        btnLoginNormal = (Button) findViewById(R.id.btnLoginNormal);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnFacebookLogin = (CircularImageView) findViewById(R.id.btnFacebookLogin);
        btnGooglePlusLogin = (CircularImageView) findViewById(R.id.btnGooglePlusLogin);
    }

    private void click() {
        imageBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnLoginNormal.setOnClickListener(this);
        etPassword.setOnClickListener(this);
        etUsername.setOnClickListener(this);
        btnFacebookLogin.setOnClickListener(this);
        btnGooglePlusLogin.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.btnLoginNormal:
                actionLoginNormal();
                break;
            case R.id.btnFacebookLogin:
                ToastCustom.show(getApplicationContext(), "Tính năng này chưa sẵn sàng", ToastCustom.LENGTH_SHORT, ToastCustom.INF0);
                //actionLoginFacebook();
                break;
            case R.id.btnGooglePlusLogin:
                ToastCustom.show(getApplicationContext(), "Tính năng này chưa sẵn sàng", ToastCustom.LENGTH_SHORT, ToastCustom.INF0);
                // actionLoginGooglePlus();
                break;
            case R.id.imgToolbarBack:
                super.onBackPressed();
                break;
        }
    }

    //    private void actionLoginGooglePlus() {
    //
    //    }
    //
    //    private void actionLoginFacebook() {
    //        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(
    //                "public_profile", "email", "user_birthday"));
    //        LoginManager.getInstance().registerCallback(callbackManager,
    //                new FacebookCallback<LoginResult>() {
    //                    @Override
    //                    public void onSuccess(LoginResult loginResult) {
    //                        String accessToken = loginResult.getAccessToken().getToken();
    //                        //Log.i("accessToken", accessToken);
    //                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
    //
    //                            @Override
    //                            public void onCompleted(JSONObject object, GraphResponse response) {
    //                                //System.out.println("-------LoginActivity: " + response.toString());
    //                                //System.out.println("--------DATA:" + object);
    //                                // Get facebook data from login
    //                                getFacebookData(object);
    //                            }
    //                        });
    //
    //                        Bundle parameters = new Bundle();
    //                        parameters.putString("fields", "id,name,email,gender,birthday"); // Parámetros que pedimos a facebook
    //                        request.setParameters(parameters);
    //                        request.executeAsync();
    //                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
    //
    //                    }
    //
    //                    @Override
    //                    public void onCancel() {
    //                        Toast.makeText(getApplicationContext(), "Login attempt canceled.", Toast.LENGTH_LONG).show();
    //                    }
    //
    //                    @Override
    //                    public void onError(FacebookException exception) {
    //                        Toast.makeText(getApplicationContext(), "Login attempt failed.", Toast.LENGTH_LONG).show();
    //                    }
    //                });
    //    }

    private void actionLoginNormal() {
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString();

        //Xác thực Input
        if (token != null && username.length() == 0 || password.length() == 0) {
            ToastCustom.show(getApplicationContext(), "Vui lòng điền đầy đủ vào các trường", ToastCustom.LENGTH_LONG, ToastCustom.WARNING);
            return;
        }
        //Login server
        if (!isLoading) {
            isLoading = true;
            RequestParams params = new RequestParams();
            params.put("t", "lg");
            params.put("usr", username);
            params.put("pwd", Md5.Hash(password));
            params.put("au", token);

            Tool.post(getResources().getString(R.string.api_user_srv), params, new JsonHttpResponseHandler() {
                @Override
                public void onFinish() {
                    isLoading = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    ToastCustom.show(getApplicationContext(), "Error: " + String.valueOf(statusCode), ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    ToastCustom.show(getApplicationContext(), "Error: " + String.valueOf(statusCode), ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //                            System.out.println("----------------" + response);
                    try {
                        if (!response.has("status")) {
                            ToastCustom.show(getApplicationContext(), "Lỗi, xin hãy thử lại vào lúc khác ", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                            return;
                        }

                        int status = response.getInt("status");  //Kiểm tra trạng thái Đăng nhập

                        switch (status) {
                            case 1:                             //Thành công
                                saveNormalData(response);        //Lưu thông tin user
                                //                                if (!returnResult) {
                                //                                    startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                //                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                //                                    finish();
                                //                                } else {
                                //                                    Intent returnIntent = new Intent();
                                //                                    setResult(Activity.RESULT_OK, returnIntent);//900 code
                                //                                    finish();
                                //                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                //                                }
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);//900 code
                                finish();
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                break;
                            case 2:
                                ToastCustom.show(getApplicationContext(), "Phiên làm việc hết hạn. Bạn hãy đăng nhập lại", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                                break;
                            default:
                                //Các lỗi khác: Gửi thông điệp báo lỗi
                                String message = (response.has("message")) ? response.getString("message") : "Lỗi đăng nhập";
                                ToastCustom.show(getApplicationContext(), message, ToastCustom.LENGTH_LONG, ToastCustom.ERROR);

                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastCustom.show(getApplicationContext(), "Lỗi, xin hãy thử lại vào lúc khác ", ToastCustom.LENGTH_LONG, ToastCustom.ERROR);
                    }

                }

            });
        }
    }


    private void saveNormalData(JSONObject response) {
        //System.out.println("---------------LOGIN: " + response);
        try {
            JSONObject data = response.getJSONObject("data");

            String username = data.has("username") ? data.getString("username") : "";
            String email = data.has("email") ? data.getString("email") : "";
            String token = data.has("token") ? data.getString("token") : "";
            String name = data.has("fullname") ? data.getString("fullname") : "";
            int user_id = data.has("id") ? data.getInt("id") : -1;
            String phone = data.has("mobile") ? data.getString("mobile") : "";
            String avatar = data.has("avatar") ? data.getString("avatar") : "";
            int gender = data.has("gender") ? data.getInt("gender") : 1;

            try {
                pref.edit().putString("username", username).commit();
                pref.edit().putString("email", email).commit();
                pref.edit().putString("user_id", String.valueOf(user_id)).commit();
                pref.edit().putString("gender", String.valueOf(gender)).apply();
                pref.edit().putString("login_type", "normal").apply();
                pref.edit().putString("name", name).apply();
                pref.edit().putString("token", token).apply();
                pref.edit().putString("phone", phone).apply();
                pref.edit().putString("avatar", avatar).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    private void getFacebookData(JSONObject object) {
    //        try {
    //            String id = object.getString("id");
    //            final String name = object.has("name") ? object.getString("name") : "";
    //            final String email = object.has("email") ? object.getString("email") : "";
    //            final String gender = object.has("gender") ? object.getString("gender") : "male";
    //
    //            try {
    //                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=200");
    //                Log.i("profile_pic", profile_pic + "");
    //                downloadFile(profile_pic.toString(), email);
    //            } catch (MalformedURLException e) {
    //                e.printStackTrace();
    //            }
    //
    //
    //            RequestParams params = new RequestParams();
    //            params.put("email", email);
    //            post(getResources().getString(R.string.path_user_request_token), params, new JsonHttpResponseHandler() {
    //                @Override
    //                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
    //
    //                    try {
    //                        int status = response.getInt("status");
    //                        // String message = response.getString("message");
    //                        if (status == 1) {
    //                            JSONObject data = response.getJSONObject("data");
    //
    //                            String username = data.has("username") ? data.getString("username") : "";
    //                            String email = data.has("email") ? data.getString("email") : "";
    //                            String token = data.getString("token");
    //                            int user_id = data.has("id") ? data.getInt("id") : -1;
    //                            int gender = data.has("gender") ? data.getInt("gender") : 1;
    //
    //                            try {
    //                                pref.edit().putString("username", username).apply();
    //                                pref.edit().putString("email", email).apply();
    //                                pref.edit().putString("user_id", String.valueOf(user_id)).apply();
    //                                pref.edit().putString("gender", String.valueOf(gender)).apply();
    //                                pref.edit().putString("login_type", "facebook").apply();
    //                                pref.edit().putString("token", token).apply();
    //                            } catch (Exception e) {
    //                                e.printStackTrace();
    //                            }
    //                            finish();
    //                        } else {
    //                            //Email ko tồn tại (Yêu cầu đăng ký tài khoản)
    //                            //Chuyển sang trang đăng ký với thông tin email và username lấy từ Facebook
    //                            String username = name.replaceAll("\\s+", "").toLowerCase();
    //                            int sex = (gender.equals("male")) ? 1 : 0;
    //                            Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
    //                            intent.putExtra("email", email);
    //                            intent.putExtra("username", username);
    //                            intent.putExtra("gender", sex);
    //                            startActivity(intent);
    //                        }
    //                    } catch (JSONException e) {
    //                        e.printStackTrace();
    //                    }
    //                }
    //
    //                @Override
    //                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
    //                    super.onFailure(statusCode, headers, responseString, throwable);
    //                }
    //            });
    //        } catch (JSONException e) {
    //            e.printStackTrace();
    //        }
    //
    //    }
    //
    //    public void downloadFile(String imgUrl, String imagename) {
    //        if (ImageStorage.checkifImageExists(imagename)) {
    //            File file = ImageStorage.getImage("/" + imagename + ".jpg");
    //            String path = file.getAbsolutePath();
    //            if (path != null) {
    //                // b = BitmapFactory.decodeFile(path);
    //                //  imageView.setImageBitmap(b);
    //            }
    //        } else {
    //            new GetImagesTask(imgUrl, imagename).execute();
    //        }
    //
    //    }

}
