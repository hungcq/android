package raijin.session14_webservice;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "1d0c9ee28484e62b8e883736a30b7468";
    private static final String API = "http://api.openweathermap.org/data/2.5/weather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadJson downloadJson = new DownloadJson();
        downloadJson.execute(API+"?q=Hanoi&&appid="+API_KEY);
    }

    public class DownloadJson extends AsyncTask <String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            InputStream inputStream;
            HttpURLConnection httpURLConnection;
            String result = "";
            try {
                URL url = new URL(strings[0]);
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
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null)  {
                JsonModel jsonModel = (new Gson().fromJson(s,JsonModel.class));
                Log.d("Main: ", jsonModel.getJsonWeatherModel().get(0).getMain());
                Log.d("Description: ", jsonModel.getJsonWeatherModel().get(0).getDescription());
                Log.d("Temp: ", jsonModel.getJsonMainModel().getTemp() + "");
                Log.d("Pressure: ", jsonModel.getJsonMainModel().getPressure() + "");
                Log.d("Humidity: ", jsonModel.getJsonMainModel().getHumidity() + "");
            }
        }
    }
}
