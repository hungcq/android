package raijin.session4_hw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 1918 on 08-Jul-16.
 */
public class DetailsActivity extends AppCompatActivity {

    private TextView city;
    private TextView temperature;
    private TextView humidity;
    private ImageView mainWeather;
    private TextView windSpeed;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        city = (TextView) findViewById(R.id.name);
        temperature = (TextView) findViewById(R.id.tem);
        mainWeather = (ImageView) findViewById(R.id.main_weather);
        humidity = (TextView) findViewById(R.id.humidity);
        windSpeed = (TextView) findViewById(R.id.wind_speed);

        Bundle bundle = getIntent().getExtras();
        city.setText(bundle.getString("city"));
        double temp = Double.parseDouble(bundle.getString("temp"))-273;
        int roundedTemp = (int) Math.floor(temp);
        temperature.setText(Integer.toString(roundedTemp)+"°C");
        humidity.setText(bundle.getString("humidity")+"%");
        mainWeather.setImageResource(R.mipmap.partly_sunny);
        windSpeed.setText(bundle.getString("wind_speed") + " mps");
    }

}
