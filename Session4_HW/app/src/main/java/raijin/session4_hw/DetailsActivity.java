package raijin.session4_hw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 1918 on 08-Jul-16.
 */
public class DetailsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String tem = bundle.getString("tem")+"ºC";
        int imageResource = bundle.getInt("imageResource");

        TextView cityName = (TextView) findViewById(R.id.name);
        TextView temperature = (TextView) findViewById(R.id.tem);
        ImageView mainWeather = (ImageView) findViewById(R.id.main_weather);

        cityName.setText(name);
        temperature.setText(tem);
        mainWeather.setImageResource(imageResource);
    }
}
