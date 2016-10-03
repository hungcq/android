package raijin.session16_weatherapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String iconUrl;
    private ImageView iconImage;
    private TextView temperatureTextView;
    private TextView temperatureDetailsTextView;
    private TextView cityTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String FILE_NAME = "WeatherData";
    private String mainWeather = "";
    private Context context;
    private final String[] city = {"hanoi", "seatle", "moscow"};
    private int position;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        iconImage = (ImageView) view.findViewById(R.id.weather_icon);
        temperatureTextView = (TextView) view.findViewById(R.id.temperature);
        temperatureDetailsTextView = (TextView) view.findViewById(R.id.tem_details);
        cityTextView = (TextView) view.findViewById(R.id.city);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        sharedPreferences = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setCityName();
    }

    public void setCity(int position) {
        this.position = position;
    }

    @Override
    public void onResume() {
        super.onResume();
        getWeatherData();
    }

    protected void getWeatherData() {
        if (isNetworkConnected()) {
            GetService getService = ServiceFactory.getInst().createService(GetService.class);
            Call<JsonModel> call = getService.callJson(city[position], "1d0c9ee28484e62b8e883736a30b7468");
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

                        Picasso.with(context).load(iconUrl).into(iconImage);

                        long temperature = Math.round(Double.parseDouble(temp) - 273);
                        long minTemperature = Math.round(Double.parseDouble(minTemp) - 273);
                        long maxTemperature = Math.round(Double.parseDouble(maxTemp) - 273);
                        temperatureTextView.setText(Long.toString(temperature) + "°C");
                        temperatureDetailsTextView.setText("Low " + Long.toString(minTemperature) + "°C" + "   High " + Long.toString(maxTemperature) + "°C");
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

    private void setCityName() {
        if (position == 0) {
            cityTextView.setText("Hanoi");
        } else if (position == 1) {
            cityTextView.setText("Seatle");
        } else if (position == 2) {
            cityTextView.setText("Moscow");
        }
    }

    private void saveData() {
        editor.putString("temp", temperatureTextView.getText().toString());
        editor.putString("temp_details", temperatureDetailsTextView.getText().toString());
        editor.putString("main_weather", mainWeather);
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
        temperatureTextView.setText(sharedPreferences.getString("temp", ""));
        temperatureDetailsTextView.setText(sharedPreferences.getString("temp_details", ""));
        mainWeather = sharedPreferences.getString("main_weather", "");
        loadBackgroundImage();
    }

    @Override
    public void onRefresh() {
        getWeatherData();
        swipeRefreshLayout.setRefreshing(false);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
