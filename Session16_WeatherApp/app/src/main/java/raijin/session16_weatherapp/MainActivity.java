package raijin.session16_weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String iconUrl;
    private ImageView iconImage;
    private TextView temperatureTextView;
    private TextView temperatureDetailsTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String FILE_NAME = "WeatherData";
    private String mainWeather = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        iconImage = (ImageView) findViewById(R.id.weather_icon);
        temperatureTextView = (TextView) findViewById(R.id.temperature);
        temperatureDetailsTextView = (TextView) findViewById(R.id.tem_details);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        sharedPreferences = this.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherData();
    }

    protected void getWeatherData() {
        if(isNetworkConnected()) {
            GetService getService = ServiceFactory.getInst().createService(GetService.class);
            Call<JsonModel> call = getService.callJson("hanoi", "1d0c9ee28484e62b8e883736a30b7468");
            call.enqueue(new Callback<JsonModel>() {
                @Override
                public void onResponse(Call<JsonModel> call, Response<JsonModel> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        JsonModel jsonModel = response.body();
                        mainWeather = jsonModel.getJsonWeatherModel().get(0).getMain();
                        String temp = jsonModel.getJsonMainModel().getTemp() + "";
                        String icon = jsonModel.getJsonWeatherModel().get(0).getIcon();
                        String minTemp = jsonModel.getJsonMainModel().getMinTemp() + "";
                        String maxTemp = jsonModel.getJsonMainModel().getMaxTemp() + "";
                        iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";

                        Picasso.with(MainActivity.this).load(iconUrl).into(iconImage);

                        int temperature = (int) Double.parseDouble(temp) - 273;
                        int minTemperature = (int) Double.parseDouble(minTemp) - 273;
                        int maxTemperature = (int) Double.parseDouble(maxTemp) - 273;
                        temperatureTextView.setText(Integer.toString(temperature) + "°C");
                        temperatureDetailsTextView.setText("Low " + Integer.toString(minTemperature) + "°C" + "   High " + Integer.toString(maxTemperature) + "°C");

                        loadBackgroundImage();
                        saveData();
                    }
                }

                @Override
                public void onFailure(Call<JsonModel> call, Throwable t) {

                }
            });
        } else loadData();
    }

    private void saveData() {
        editor.putString("temp",temperatureTextView.getText().toString());
        editor.putString("temp_details",temperatureDetailsTextView.getText().toString());
        editor.putString("main_weather",mainWeather);
        editor.commit();
    }

    private void loadBackgroundImage() {
        if (mainWeather.equals("Clear")) {
            swipeRefreshLayout.setBackgroundResource(R.mipmap.clear);
        } else if (mainWeather.equals("Rain")) {
            swipeRefreshLayout.setBackgroundResource(R.mipmap.rainy);
        } else if (mainWeather.equals("Clouds"))
            swipeRefreshLayout.setBackgroundResource(R.mipmap.cloudy);
    }

    private void loadData() {
        temperatureTextView.setText(sharedPreferences.getString("temp",""));
        temperatureDetailsTextView.setText(sharedPreferences.getString("temp_details",""));
        mainWeather = sharedPreferences.getString("main_weather","");
        loadBackgroundImage();
    }

    @Override
    public void onRefresh() {
        getWeatherData();
        swipeRefreshLayout.setRefreshing(false);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
