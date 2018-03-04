package raijin.doitlater.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import raijin.doitlater.R;
import raijin.doitlater.utils.Utils;

public class OpenMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapClickListener, View.OnClickListener {

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location currentLocation;
    private static final String defaultLocation = "Hanoi";
    private MarkerOptions targetMarker;
    private EditText searchLocationEditText;
    private Button searchLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_map);
        initialize();
    }

    private void initialize() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        searchLocationButton = (Button) findViewById(R.id.search_location_button);
        searchLocationButton.setOnClickListener(this);
        searchLocationEditText = (EditText) findViewById(R.id.search_location_edit_text);
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.child_toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setTitle(R.string.open_map_activity_title);
        ab.setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(R.anim.open_map_activity, R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_save_button_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                //TODO nhét vào đây nữa
                Intent intent = new Intent(this, NoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", getIntent().getExtras().getString("title"));
                bundle.putString("detail", getIntent().getExtras().getString("detail"));
                bundle.putString("date_reminder", getIntent().getExtras().getString("date_reminder"));
                bundle.putString("time_reminder", getIntent().getExtras().getString("time_reminder"));
                bundle.putString("image_path", getIntent().getExtras().getString("image_path"));
                bundle.putInt("priority", getIntent().getExtras().getInt("priority"));
                bundle.putInt("pathOfAudio", getIntent().getExtras().getInt("pathOfAudio"));
                if (NoteActivity.activityType == ActivityType.EDITNOTE) {
                    bundle.putInt("ID", getIntent().getExtras().getInt("ID"));
                }
                if (targetMarker != null) {
                    String location = getAddressFromLatLng(targetMarker.getPosition());
                    bundle.putString("location", location);
                }
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        initListeners();
    }

    private void initListeners() {
        map.setOnMarkerClickListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnInfoWindowClickListener(this);
        map.setOnMapClickListener(this);
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
        } catch (IOException e) {
        }

        return address;
    }

    private Location getLocationFromAddress(String location) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        Location location1 = new Location("test");
        try {
            addresses = geocoder.getFromLocationName(location, 1);
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                location1.setLatitude(latitude);
                location1.setLongitude(longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location1;
    }

    private void initCamera(Location location) {
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(),
                        location.getLongitude()))
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        initCamera(currentLocation);
        // else initCamera(getLocationFromAddress(defaultLocation));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        map.clear();
        MarkerOptions options = new MarkerOptions().position(latLng);
        options.title(getAddressFromLatLng(latLng));
        options.icon(BitmapDescriptorFactory.defaultMarker());
        targetMarker = options;
        map.addMarker(options);
        //
        String location = getAddressFromLatLng(latLng);
        searchLocationEditText.setText(location);
    }

    @Override
    public void onClick(View view) {
        String location = searchLocationEditText.getText().toString();
        switch (view.getId()) {
            case R.id.search_location_button:
                if (!location.equals(""))
                    initCamera(getLocationFromAddress(location));
                break;
        }
    }
}
