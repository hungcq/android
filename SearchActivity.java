package com.online.foodplus.activities;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.google.maps.android.PolyUtil;
import com.online.foodplus.R;
import com.online.foodplus.adapters.FindMoreRecyclerViewAdapter;
import com.online.foodplus.adapters.MarkerInfoRecyclerViewAdapter;
import com.online.foodplus.adapters.SearchAutoCompleteAdapter;
import com.online.foodplus.models.Base;
import com.online.foodplus.models.jsondirectionmodels.JsonDirectionModel;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.webservices.DirectionServiceFactory;
import com.online.foodplus.webservices.GoogleDirectionAPI;
import com.online.foodplus.webservices.PlacesAPI;
import com.online.foodplus.webservices.PlacesServiceFactory;
import com.online.foodplus.widgets.DelayAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class


SearchActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location currentLocation;
    private Location defaultLocation;
    private final float CAMERA_ZOOM_LEVEL = 15f;
    private RecyclerView recyclerView;
    private MarkerInfoRecyclerViewAdapter adapter;
    private RecyclerView findMoreRecyclerView;
    private FindMoreRecyclerViewAdapter findMoreAdapter;
    private List<Base> dataList;
    private Marker currentActiveMarker;
    private Polyline currentPolyline;
    private ImageButton resizeButton;
    private ImageButton locateButton;
    private boolean resizeToggle = false;
    private Toolbar toolbar;
    private Spinner spinner;
    private RelativeLayout spinnerLayout;
    private LinearLayout layoutBelow;
    private int DEFAULT_MARKER_SIZE;

    ////////////////////////////////////////////////
    private SharedPreferences pref;
    private TextView tvCity;
    private String id;
    private ImageView imgConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        initToolbar();
        listener();
        defaultData();
    }

    private void initToolbar() {
        DelayAutoCompleteTextView autoFind = (DelayAutoCompleteTextView) findViewById(R.id.autoFind);
        autoFind.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);                  //Cài đặt chiều rộng tối đa của Danh sách gợi ý (bằng với chiều rộng thiết bị)
        autoFind.setDropDownHeight(getResources().getDisplayMetrics().heightPixels / 2);            //Cài đặt chiều cao tối đa của Danh sách gợi ý (50% full height)
        autoFind.setAdapter(new SearchAutoCompleteAdapter(this));
        autoFind.setThreshold(2);                                                                   //Nhập tối thiểu 2 ký tự thì bắt đầu tìm kiếm
        autoFind.setLoadingIndicator(
                (android.widget.ProgressBar) findViewById(R.id.progressBar));                       //Loading Icon
        autoFind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), DetailActivity.class).putExtra("id", position));
            }
        });

        imgConfig = (ImageView) findViewById(R.id.imgConfig);
        tvCity = (TextView) findViewById(R.id.tvCity);
    }

    private void listener() {
        imgConfig.setOnClickListener(this);
        tvCity.setOnClickListener(this);
    }

    private void defaultData() {
        //Đọc dữ liệu Tỉnh thành đã lưu trong SharePreference
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        id = pref.getString("city_id", null);
        String city_title = pref.getString("city_title", getResources().getString(R.string.all));
        tvCity.setText(city_title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data.hasExtra("id") && data.hasExtra("title")) {
                id = data.getStringExtra("id");
                String title = data.getStringExtra("title");
                tvCity.setText(title);
                pref.edit().putString("city_id", id).apply();
                pref.edit().putString("city_title", title).apply();
            }
        }
    }

    private void init() {
        DEFAULT_MARKER_SIZE = Tool.getScreenWidth(this) / 15;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
        defaultLocation = new Location("");
        defaultLocation.setLatitude(21.028928);
        defaultLocation.setLongitude(105.836304);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if(position != -1) {
                        selectMarker(dataList.get(position).getMarker());
                    }
                }
            }
        });

        findMoreRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_find_more);
        findMoreRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        List<String> imageList2 = new ArrayList<>();
        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        findMoreAdapter = new FindMoreRecyclerViewAdapter(imageList2);
        findMoreRecyclerView.setAdapter(findMoreAdapter);

        resizeButton = (ImageButton) findViewById(R.id.button_resize);
        resizeButton.setOnClickListener(this);
        locateButton = (ImageButton) findViewById(R.id.button_locate);
        locateButton.setOnClickListener(this);

        spinnerLayout = (RelativeLayout) findViewById(R.id.layout_spinner);
        spinner = (Spinner) findViewById(R.id.spinner);
        List<String> places = new ArrayList<>();
        places.add("0.5 km");
        places.add("1 km");
        places.add("2 km");
        places.add("5 km");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, places);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutBelow = (LinearLayout) findViewById(R.id.layout_below);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_retro));
        initMapListener();
    }

    private void initMapListener() {
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
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
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        selectMarker(marker);
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getMarker().equals(marker)) {
                recyclerView.smoothScrollToPosition(i);
            }
        }
        return true;
    }

    private void selectMarker(Marker marker) {
        if (currentActiveMarker != null) {
            currentActiveMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(resizeMapIcons(R.drawable.ic_marker, DEFAULT_MARKER_SIZE, DEFAULT_MARKER_SIZE)));
        }
        currentActiveMarker = marker;
        currentActiveMarker.setIcon(BitmapDescriptorFactory
                .fromBitmap(resizeMapIcons(R.drawable.ic_marker_selected, DEFAULT_MARKER_SIZE * 6 / 5, DEFAULT_MARKER_SIZE * 6 / 5)));
        String from = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        String to = marker.getPosition().latitude + "," + marker.getPosition().longitude;
        GoogleDirectionAPI googleDirectionAPI = DirectionServiceFactory.getInst().createService(GoogleDirectionAPI.class);
        Call<JsonDirectionModel> call = googleDirectionAPI.callDirection(from, to, getString(R.string.google_maps_key));
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
                        currentPolyline = map.addPolyline(
                                new PolylineOptions().addAll(decodedPath).color(getResources().getColor(R.color.colorPrimaryDark)));
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonDirectionModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (Tool.isLocationEnabled(this)) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (currentLocation == null)
                currentLocation = defaultLocation;
            initCamera(currentLocation);
        } else {
            Tool.buildAlertMessageNoGps(this);
            currentLocation = defaultLocation;
            initCamera(currentLocation);
        }
        addMarkerToMap();
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_resize:
                if (!resizeToggle) {
                    resizeToggle = true;
                    resizeButton.setImageResource(R.drawable.ic_shrink);
                    hideTest();
                } else {
                    resizeToggle = false;
                    resizeButton.setImageResource(R.drawable.ic_expand);
                    showTest();
                }
                break;
            case R.id.button_locate:
                initCamera(currentLocation);
                break;
            case R.id.imgConfig:
                startActivity(new Intent(getApplicationContext(), FindActivity.class));
                break;
            case R.id.tvCity:
                Intent intent = new Intent(getApplicationContext(), SelectCityActivity.class);
                if (id != null)
                    startActivityForResult(intent.putExtra("id", id), 100);
                else
                    startActivityForResult(intent, 100);
                break;
            default:
                break;
        }
    }

    private void hideTest() {
        ObjectAnimator transRecyclerView = ObjectAnimator.ofFloat(recyclerView, View.TRANSLATION_Y, layoutBelow.getHeight());
        ObjectAnimator hideToolbar = ObjectAnimator.ofFloat(toolbar, View.TRANSLATION_Y, -toolbar.getHeight());
        ObjectAnimator transLocateButton = ObjectAnimator.ofFloat(locateButton, View.TRANSLATION_Y, layoutBelow.getHeight());
        ObjectAnimator transResizeButton = ObjectAnimator.ofFloat(resizeButton, View.TRANSLATION_Y, -resizeButton.getHeight());
        ObjectAnimator transSpinner = ObjectAnimator.ofFloat(spinnerLayout, View.TRANSLATION_Y, -spinnerLayout.getHeight());
        ObjectAnimator hideLayout = ObjectAnimator.ofFloat(layoutBelow, View.TRANSLATION_Y, layoutBelow.getHeight());
        transRecyclerView.start();
        hideToolbar.start();
        transResizeButton.start();
        transSpinner.start();
        transLocateButton.start();
        hideLayout.start();
    }

    private void showTest() {
        ObjectAnimator transRecyclerView = ObjectAnimator.ofFloat(recyclerView, View.TRANSLATION_Y, 0);
        ObjectAnimator showToolbar = ObjectAnimator.ofFloat(toolbar, View.TRANSLATION_Y, 0);
        ObjectAnimator transResizeButton = ObjectAnimator.ofFloat(resizeButton, View.TRANSLATION_Y, 0);
        ObjectAnimator transSpinner = ObjectAnimator.ofFloat(spinnerLayout, View.TRANSLATION_Y, 0);
        ObjectAnimator transLocateButton = ObjectAnimator.ofFloat(locateButton, View.TRANSLATION_Y, 0);
        ObjectAnimator showLayout = ObjectAnimator.ofFloat(layoutBelow, View.TRANSLATION_Y, 0);
        transRecyclerView.start();
        showToolbar.start();
        transResizeButton.start();
        transSpinner.start();
        transLocateButton.start();
        showLayout.start();
    }

    private Bitmap resizeMapIcons(int icon, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), icon);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private void addMarkerToMap() {
        currentActiveMarker = null;
        PlacesAPI placesAPI = PlacesServiceFactory.getInst().createService(PlacesAPI.class);
        Call<List<Base>> call = placesAPI.callPlaces(currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                Double.parseDouble((spinner.getSelectedItem().toString()).split(" ")[0]));
        call.enqueue(new Callback<List<Base>>() {
            @Override
            public void onResponse(Call<List<Base>> call, Response<List<Base>> response) {
                if (response.isSuccessful()) {
                    map.clear();
                    dataList = response.body();
                    for (Base jsonPlaceModel : dataList) {
                        MarkerOptions markerOptions = new MarkerOptions().position(
                                new LatLng(Double.parseDouble(jsonPlaceModel.getLatitude()),
                                        Double.parseDouble(jsonPlaceModel.getLongitude())));
                        markerOptions.icon(BitmapDescriptorFactory
                                .fromBitmap(resizeMapIcons(R.drawable.ic_marker, DEFAULT_MARKER_SIZE, DEFAULT_MARKER_SIZE)));
                        jsonPlaceModel.setMarker(map.addMarker(markerOptions));
                        adapter = new MarkerInfoRecyclerViewAdapter(dataList);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Base>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        addMarkerToMap();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
