package raijin.wherethefuckareyou;

import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    private TextView latTextView;
    private TextView longTextView;
    private Button refreshButton;
    private Location currentLocation;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        latTextView = (TextView) findViewById(R.id.tv_lat);
        longTextView = (TextView) findViewById(R.id.tv_long);
        refreshButton = (Button) findViewById(R.id.btn_refresh);
        refreshButton.setOnClickListener(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        latTextView.setText("Latitude: " + currentLocation.getLatitude());
        longTextView.setText("Longitude: " + currentLocation.getLongitude());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh:
                currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                latTextView.setText("Latitude: " + currentLocation.getLatitude());
                longTextView.setText("Longitude: " + currentLocation.getLongitude());
                break;
        }
    }
}
