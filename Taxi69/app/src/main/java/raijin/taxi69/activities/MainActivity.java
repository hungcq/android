package raijin.taxi69.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import raijin.taxi69.Constants;
import raijin.taxi69.R;
import raijin.taxi69.Utils;
import raijin.taxi69.adapters.ViewPagerAdapter;
import raijin.taxi69.models.DoubleArrayEvaluator;
import raijin.taxi69.models.TaxiInfo;
import raijin.taxi69.models.TestPoint;
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

    private DatabaseReference taxiChildReference;
    private DatabaseReference testPointChildReference;
    private Map<String, TaxiInfo> taxiInfoMap;
    private List<TestPoint> testPointList;

    private TextView userName;
    private TextView userEmail;
    private ImageView userAvatar;


    private Location location;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

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

        userAvatar = (ImageView) findViewById(R.id.img_user_avatar);
        userName = (TextView) findViewById(R.id.tv_user_name);
        userEmail = (TextView) findViewById(R.id.tv_user_email);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        bottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        setUpViewPagerAndTabLayout();

        pickUpInfoBox = (CustomInfoBox) findViewById(R.id.info_box_pick_up);
        pickUpInfoBox.initData(R.drawable.blue_point, "Pick-up point", Utils.getAddressFromLatLng(this, new LatLng(DEFAULT_LAT, DEFAULT_LNG)));
        pickUpInfoBox.setOnClickListener(this);
        dropOffInfoBox = (CustomInfoBox) findViewById(R.id.info_box_drop_off);
        dropOffInfoBox.initData(R.drawable.yellow_point, "Drop-off point", Utils.getAddressFromLatLng(this, new LatLng(DEFAULT_LAT, DEFAULT_LNG)));
        dropOffInfoBox.setOnClickListener(this);

        taxiChildReference = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_TAXI);
        taxiInfoMap = new HashMap<>();

        testPointChildReference = FirebaseDatabase.getInstance().getReference().child(Constants.CHILD_TEST_POINT);
        testPointList = new ArrayList<>();
        testPointChildReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TestPoint testPoint = dataSnapshot.getValue(TestPoint.class);
                testPointList.add(testPoint);
//                taxiChildReference.child(dataSnapshot.getKey()).setValue(new TaxiInfo(12000, 1, testPoint.getLatitude(), testPoint.getLongitude(), 0));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void authencation() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        } else {
            userName.setText(firebaseUser.getDisplayName());
            userEmail.setText(firebaseUser.getEmail());
            if (firebaseUser.getPhotoUrl() != null) {
                Picasso.with(this).load(firebaseUser.getPhotoUrl()).into(userAvatar);
            }
        }
    }

    private void setUpViewPagerAndTabLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("City"));
        tabLayout.addTab(tabLayout.newTab().setText("AirPort"));
        tabLayout.addTab(tabLayout.newTab().setText("HD"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        authencation();
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

        taxiChildReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TaxiInfo taxiInfo = dataSnapshot.getValue(TaxiInfo.class);
                addCarMarker(taxiInfo);
                taxiInfoMap.put(dataSnapshot.getKey(), taxiInfo);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                TaxiInfo taxiInfo = taxiInfoMap.get(dataSnapshot.getKey());
                moveMarker(taxiInfo.getMarker(), dataSnapshot.getValue(TaxiInfo.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                taxiInfoMap.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        setAddress(pickUpInfoBox, latLng);

        final Handler handler = new Handler();
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });

            }
        }, 0, 5, TimeUnit.SECONDS);
//        testPointChildReference.push().setValue(new TestPoint(latLng.latitude, latLng.longitude));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Location location = new Location("");
        location.setLatitude(marker.getPosition().latitude);
        location.setLongitude(marker.getPosition().longitude);
        drawPolyline(currentLocation, location);
        setAddress(pickUpInfoBox, marker.getPosition());
        return false;
    }

    private void setAddress(final CustomInfoBox customInfoBox, final LatLng latLng) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String address = Utils.getAddressFromLatLng(MainActivity.this, latLng);
                getWindow().getDecorView().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        customInfoBox.setAddress(address);
                    }
                });
            }
        }).start();
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

    private void addCarMarker(TaxiInfo taxiInfo) {
        MarkerOptions options = new MarkerOptions().position(new LatLng(taxiInfo.getLatitude(), taxiInfo.getLongitude()));
        options.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.ic_car, 30, 60)));
        Marker marker = map.addMarker(options);
        marker.setRotation((float) taxiInfo.getAngle());
        taxiInfo.setMarker(marker);
    }

    private Bitmap resizeMapIcons(int icon, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), icon);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private void moveMarker(final Marker marker, TaxiInfo taxiInfo) {
        if (marker != null) {
            double startAngle = marker.getRotation();
            double finalAngle = SphericalUtil.computeHeading(marker.getPosition(),
                    new LatLng(taxiInfo.getLatitude(), taxiInfo.getLongitude()));
            if (finalAngle - startAngle > 180) {
                startAngle += 360;
            }
            ValueAnimator angleAnimator = ValueAnimator.ofFloat((float) startAngle,
                    (float) finalAngle);
            angleAnimator.setDuration(1000);
            angleAnimator.setInterpolator(new LinearInterpolator());
            angleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    marker.setRotation((float) animation.getAnimatedValue());
                    Log.d("updated", animation.getAnimatedValue().toString());
                }
            });

            double[] startValues = new double[]{marker.getPosition().latitude, marker.getPosition().longitude};
            double[] endValues = new double[]{taxiInfo.getLatitude(), taxiInfo.getLongitude()};
            final ValueAnimator latLngAnimator = ValueAnimator.ofObject(new DoubleArrayEvaluator(), startValues, endValues);
            latLngAnimator.setDuration(4000);
            latLngAnimator.setInterpolator(new LinearInterpolator());
            latLngAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    double[] animatedValue = (double[]) animation.getAnimatedValue();
                    marker.setPosition(new LatLng(animatedValue[0], animatedValue[1]));
                }
            });
            angleAnimator.start();
            angleAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    latLngAnimator.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }
}
