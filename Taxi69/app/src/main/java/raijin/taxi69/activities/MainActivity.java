package raijin.taxi69.activities;

import android.animation.ObjectAnimator;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

import raijin.taxi69.R;
import raijin.taxi69.Utils;
import raijin.taxi69.models.jsondirectionmodels.JsonDirectionModel;
import raijin.taxi69.webservices.DirectionAPI;
import raijin.taxi69.webservices.DirectionServiceFactory;
import raijin.taxi69.widgets.CustomInfoBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location currentLocation;
    private Location defaultLocation;
    private static final float CAMERA_ZOOM_LEVEL = 15f;
    private static final double DEFAULT_LAT = 21.028928;
    private static final double DEFAULT_LNG = 105.836304;
    private Marker currentActiveMarker;
    private Polyline currentPolyline;
    private ImageButton resizeButton;
    private boolean resizeToggle = false;
    private ImageButton myLocationButton;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RelativeLayout bottomLayout;
    private CustomInfoBox pickUpInfoBox;
    private CustomInfoBox dropOffInfoBox;

    private Location location;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        defaultLocation = new Location("");
        defaultLocation.setLatitude(DEFAULT_LAT);
        defaultLocation.setLongitude(DEFAULT_LNG);

        resizeButton = (ImageButton) findViewById(R.id.btn_resize);
        resizeButton.setOnClickListener(this);
        myLocationButton = (ImageButton) findViewById(R.id.btn_my_location);
        myLocationButton.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        bottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);

        pickUpInfoBox = (CustomInfoBox) findViewById(R.id.info_box_pick_up);
        pickUpInfoBox.initData(R.drawable.blue_point, "Pick-up point", Utils.getAddressFromLatLng(this, new LatLng(DEFAULT_LAT, DEFAULT_LNG)));
        pickUpInfoBox.setOnClickListener(this);
        dropOffInfoBox = (CustomInfoBox) findViewById(R.id.info_box_drop_off);
        dropOffInfoBox.initData(R.drawable.yellow_point, "Drop-off point", Utils.getAddressFromLatLng(this, new LatLng(DEFAULT_LAT, DEFAULT_LNG)));
        dropOffInfoBox.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_silver));
        initMapListener();
    }

    private void initMapListener() {
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
    }

    private void initCamera(Location location) {
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude() - 0.0015,
                        location.getLongitude()))
                .zoom(CAMERA_ZOOM_LEVEL)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        map.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_resize:
                if (!resizeToggle) {
                    resizeToggle = true;
                    resizeButton.setImageResource(R.drawable.ic_collapse);
                    expandMap();
                } else {
                    resizeToggle = false;
                    resizeButton.setImageResource(R.drawable.ic_expand);
                    shrinkMap();
                }
                break;
        }
    }

    private void expandMap() {
        ObjectAnimator transToolbar = ObjectAnimator.ofFloat(toolbar, View.TRANSLATION_Y, -toolbar.getHeight());
        ObjectAnimator transResizeButton = ObjectAnimator.ofFloat(resizeButton, View.TRANSLATION_Y, -toolbar.getHeight());
        ObjectAnimator transMyLocationButton = ObjectAnimator.ofFloat(myLocationButton, View.TRANSLATION_Y, myLocationButton.getHeight());
        ObjectAnimator transBottomLayout = ObjectAnimator.ofFloat(bottomLayout, View.TRANSLATION_Y, bottomLayout.getHeight());
        ObjectAnimator transPickupBox = ObjectAnimator.ofFloat(pickUpInfoBox, View.TRANSLATION_Y, -toolbar.getHeight());
        ObjectAnimator transDropoffBox = ObjectAnimator.ofFloat(dropOffInfoBox, View.TRANSLATION_Y, -toolbar.getHeight());
        transToolbar.start();
        transResizeButton.start();
        transMyLocationButton.start();
        transBottomLayout.start();
        transPickupBox.start();
        transDropoffBox.start();
    }

    private void shrinkMap() {
        ObjectAnimator transToolbar = ObjectAnimator.ofFloat(toolbar, View.TRANSLATION_Y, 0);
        ObjectAnimator transResizeButton = ObjectAnimator.ofFloat(resizeButton, View.TRANSLATION_Y, 0);
        ObjectAnimator transMyLocationButton = ObjectAnimator.ofFloat(myLocationButton, View.TRANSLATION_Y, 0);
        ObjectAnimator transBottomLayout = ObjectAnimator.ofFloat(bottomLayout, View.TRANSLATION_Y, 0);
        ObjectAnimator transPickupBox = ObjectAnimator.ofFloat(pickUpInfoBox, View.TRANSLATION_Y, 0);
        ObjectAnimator transDropoffBox = ObjectAnimator.ofFloat(dropOffInfoBox, View.TRANSLATION_Y, 0);
        transToolbar.start();
        transResizeButton.start();
        transMyLocationButton.start();
        transBottomLayout.start();
        transPickupBox.start();
        transDropoffBox.start();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (Utils.isLocationEnabled(this)) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (currentLocation == null)
                currentLocation = defaultLocation;
            initCamera(currentLocation);
        } else {
            Utils.buildAlertMessageNoGps(this);
            currentLocation = defaultLocation;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(final LatLng latLng) {
        if (currentActiveMarker != null) {
            currentActiveMarker.remove();
        }
        MarkerOptions options = new MarkerOptions().position(latLng);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
        currentActiveMarker = map.addMarker(options);
        location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        drawPolyline(currentLocation, location);
        new Thread(new Runnable() {
            @Override
            public void run() {
                address = Utils.getAddressFromLatLng(MainActivity.this, latLng);
                getWindow().getDecorView().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        pickUpInfoBox.setAddress(address);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private void drawPolyline(Location startPoint, Location endPoint) {
        String from = startPoint.getLatitude() + "," + startPoint.getLongitude();
        String to = endPoint.getLatitude() + "," + endPoint.getLongitude();
        DirectionAPI googleDirectionAPI = DirectionServiceFactory.getInst().createService(DirectionAPI.class);
        Call<JsonDirectionModel> call = googleDirectionAPI.callDirection(from, to, getString(R.string.api_key));
        call.enqueue(new Callback<JsonDirectionModel>() {
            @Override
            public void onResponse(Call<JsonDirectionModel> call, Response<JsonDirectionModel> response) {
                if (response.isSuccessful()) {
                    if (currentPolyline != null) {
                        currentPolyline.remove();
                    }
                    JsonDirectionModel jsonDirectionModel = response.body();
                    if (jsonDirectionModel.getRoutes().size() > 0) {
                        String polyline = jsonDirectionModel.getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> decodedPath = PolyUtil.decode(polyline);
                        currentPolyline = map.addPolyline(new PolylineOptions().addAll(decodedPath));
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonDirectionModel> call, Throwable t) {

            }
        });
    }
}
