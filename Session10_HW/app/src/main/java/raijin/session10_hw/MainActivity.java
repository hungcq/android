package raijin.session10_hw;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Uri intentData;
    private String encodedData;
    private Button showOnMapButton;
    private Button drivingButton;
    private Button walkingButton;
    private Button bicyclingButton;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showOnMapButton = (Button) findViewById(R.id.show_on_map_button);
        drivingButton = (Button) findViewById(R.id.driving_button);
        walkingButton = (Button) findViewById(R.id.walking_button);
        bicyclingButton = (Button) findViewById(R.id.bicycling_button);
        editText = (EditText) findViewById(R.id.location_edit_text);
        showOnMapButton.setOnClickListener(this);
        drivingButton.setOnClickListener(this);
        walkingButton.setOnClickListener(this);
        bicyclingButton.setOnClickListener(this);
    }

    protected void startIntent(Uri data) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(data);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View view) {
        if(editText.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please fill in the location", Toast.LENGTH_SHORT).show();
        } else {
            encodedData = Uri.encode(editText.getText().toString());
            switch (view.getId()) {
                case R.id.show_on_map_button:
                    intentData = Uri.parse("geo:0,0?q="+encodedData);
                    startIntent(intentData);
                    break;
                case R.id.driving_button:
                    intentData = Uri.parse("google.navigation:q="+encodedData + "&mode=d");
                    startIntent(intentData);
                    break;
                case R.id.walking_button:
                    intentData = Uri.parse("google.navigation:q="+encodedData + "&mode=w");
                    startIntent(intentData);
                    break;
                case R.id.bicycling_button:
                    intentData = Uri.parse("google.navigation:q="+encodedData + "&mode=b");
                    startIntent(intentData);
                    break;
                default:
                    break;
            }
        }
    }
}