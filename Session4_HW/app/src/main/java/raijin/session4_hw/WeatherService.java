package raijin.session4_hw;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService extends IntentService {

    private static final String API = "http://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "1d0c9ee28484e62b8e883736a30b7468";
    private String city;
    private String urlString;

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            city = intent.getStringExtra("city");
            urlString = API+"?q="+city+"&&appid="+API_KEY;
            InputStream inputStream;
            HttpURLConnection httpURLConnection;
            String result = "";
            try {
                URL url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    String inputString;
                    while ((inputString = bufferedReader.readLine()) != null) {
                        stringBuilder.append(inputString);
                    }

                    result = stringBuilder.toString();
                    httpURLConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(result != null)  {
                JsonModel jsonModel = (new Gson().fromJson(result,JsonModel.class));
                String mainWeather = jsonModel.getJsonWeatherModel().get(0).getMain();
                String description = jsonModel.getJsonWeatherModel().get(0).getDescription();
                String temp = jsonModel.getJsonMainModel().getTemp() + "";
                String pressure = jsonModel.getJsonMainModel().getPressure() + "";
                String humidity = jsonModel.getJsonMainModel().getHumidity() + "";
                String windSpeed = jsonModel.getJsonWindModel().getSpeed() + "";
                Intent intent1 = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("main_weather",mainWeather);
                bundle.putString("description",description);
                bundle.putString("temp",temp);
                bundle.putString("pressure",pressure);
                bundle.putString("humidity",humidity);
                bundle.putString("city",city);
                bundle.putString("wind_speed",windSpeed);
                intent1.putExtras(bundle);
                intent1.setAction(MainActivity.MyReceiver.RESPONE_ACTION);
                intent1.addCategory(Intent.CATEGORY_DEFAULT);
                sendBroadcast(intent1);
            }
        }
    }
}
