package com.online.foodplus.fragments;


import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

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
import com.online.foodplus.activities.SearchAdvancedActivity;
import com.online.foodplus.adapters.MarkerInfoRecyclerViewAdapter;
import com.online.foodplus.models.Base2;
import com.online.foodplus.models.jsondirectionmodels.JsonDirectionModel;
import com.online.foodplus.utils.Tool;
import com.online.foodplus.webservices.DirectionAPI;
import com.online.foodplus.webservices.DirectionServiceFactory;
import com.online.foodplus.webservices.PlacesAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static View rootView;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location currentLocation;
    private Location defaultLocation;
    private final float CAMERA_ZOOM_LEVEL = 13f;
    private int DEFAULT_MARKER_SIZE;
    private RecyclerView recyclerView;
    private MarkerInfoRecyclerViewAdapter adapter;
    //    private RecyclerView findMoreRecyclerView;
    //private FindMoreRecyclerViewAdapter findMoreAdapter;
    private List<Base2> listData;
    private Marker currentActiveMarker;
    private Polyline currentPolyline;
    private ImageButton resizeButton;
    private ImageButton locateButton;
    private boolean resizeToggle = false;
    private Spinner spinner;
    private RelativeLayout spinnerLayout;
    private LinearLayout layoutBelow;
    private SnapHelper snapHelper;

    public NearbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        init(rootView);
        return rootView;
    }

    private void init(View view) {
        DEFAULT_MARKER_SIZE = Tool.getScreenWidth(getActivity()) / 12;
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
        defaultLocation = new Location("");
        defaultLocation.setLatitude(21.028928);
        defaultLocation.setLongitude(105.836304);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        if (snapHelper == null) {
            snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (position != -1) selectMarker(listData.get(position).getMarker());
                }
            }
        });

        //        findMoreRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_find_more);
        //        findMoreRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //        List<String> imageList2 = new ArrayList<>();
        //        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        //        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        //        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        //        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        //        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        //        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        //        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        //        imageList2.add("http://media.designs.vn/public/media/media/picture/08-10-2013/thietke-noithatFrankfurt.jpg");
        //        findMoreAdapter = new FindMoreRecyclerViewAdapter(imageList2);
        //        findMoreRecyclerView.setAdapter(findMoreAdapter);

        resizeButton = (ImageButton) view.findViewById(R.id.button_resize);
        resizeButton.setOnClickListener(this);
        locateButton = (ImageButton) view.findViewById(R.id.button_locate);
        locateButton.setOnClickListener(this);

        spinnerLayout = (RelativeLayout) view.findViewById(R.id.layout_spinner);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        List<String> places = new ArrayList<>();
        places.add("0.5 km");
        places.add("1 km");
        places.add("2 km");
        places.add("5 km");
        places.add("10 km");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, places);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        layoutBelow = (LinearLayout) view.findViewById(R.id.layout_below);

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle_retro));
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
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).getMarker().equals(marker)) {
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
        DirectionAPI directionAPI = DirectionServiceFactory.getInst().createService(DirectionAPI.class);

        Call<JsonDirectionModel> call = directionAPI.callDirection(from, to, getString(R.string.google_maps_key));
        call.enqueue(new Callback<JsonDirectionModel>() {
            @Override
            public void onResponse(Call<JsonDirectionModel> call, Response<JsonDirectionModel> response) {
                if (isAdded()) {
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
        if (Tool.isLocationEnabled(getActivity())) {

            currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (currentLocation == null)
                currentLocation = defaultLocation;
            initCamera(currentLocation);
        } else {
            Tool.buildAlertMessageNoGps(getActivity());
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
            default:
                break;
        }
    }

    private void hideTest() {
        ObjectAnimator transRecyclerView = ObjectAnimator.ofFloat(recyclerView, View.TRANSLATION_Y, layoutBelow.getHeight());
        ObjectAnimator transLocateButton = ObjectAnimator.ofFloat(locateButton, View.TRANSLATION_Y, layoutBelow.getHeight());
        ObjectAnimator transResizeButton = ObjectAnimator.ofFloat(resizeButton, View.TRANSLATION_Y,
                -((SearchAdvancedActivity) getActivity()).getToolbarLayout().getHeight() * 2 / 3);
        ObjectAnimator transSpinner = ObjectAnimator.ofFloat(spinnerLayout, View.TRANSLATION_Y,
                -((SearchAdvancedActivity) getActivity()).getToolbarLayout().getHeight() * 2 / 3);
        ObjectAnimator hideLayout = ObjectAnimator.ofFloat(layoutBelow, View.TRANSLATION_Y, layoutBelow.getHeight());
        ((SearchAdvancedActivity) getActivity()).hideToolbarAndTabLayout();
        transRecyclerView.start();
        transResizeButton.start();
        transSpinner.start();
        transLocateButton.start();
        hideLayout.start();
    }

    private void showTest() {
        ObjectAnimator transRecyclerView = ObjectAnimator.ofFloat(recyclerView, View.TRANSLATION_Y, 0);
        ObjectAnimator transResizeButton = ObjectAnimator.ofFloat(resizeButton, View.TRANSLATION_Y, 0);
        ObjectAnimator transSpinner = ObjectAnimator.ofFloat(spinnerLayout, View.TRANSLATION_Y, 0);
        ObjectAnimator transLocateButton = ObjectAnimator.ofFloat(locateButton, View.TRANSLATION_Y, 0);
        ObjectAnimator showLayout = ObjectAnimator.ofFloat(layoutBelow, View.TRANSLATION_Y, 0);
        ((SearchAdvancedActivity) getActivity()).showToolbarAndTabLayout();
        transRecyclerView.start();
        transResizeButton.start();
        transSpinner.start();
        transLocateButton.start();
        showLayout.start();
    }

    private Bitmap resizeMapIcons(int icon, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), icon);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    private void addMarkerToMap() {
        currentActiveMarker = null;
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.xplay.vn")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PlacesAPI placesAPI = retrofit.create(PlacesAPI.class);
        Call<List<Base2>> call = placesAPI.callPlaces(currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                Double.parseDouble((spinner.getSelectedItem().toString()).split(" ")[0]));
        call.enqueue(new Callback<List<Base2>>() {
            @Override
            public void onResponse(Call<List<Base2>> call, Response<List<Base2>> response) {
                if (response.isSuccessful()) {
                    map.clear();
                    listData = response.body();
                    for (Base2 model : listData) {
                        MarkerOptions markerOptions = new MarkerOptions().position(
                                new LatLng(model.getLatitude(),
                                        model.getLongitude()));
                        markerOptions.icon(BitmapDescriptorFactory
                                .fromBitmap(resizeMapIcons(R.drawable.ic_marker, DEFAULT_MARKER_SIZE, DEFAULT_MARKER_SIZE)));
                        model.setMarker(map.addMarker(markerOptions));
                        adapter = new MarkerInfoRecyclerViewAdapter(listData);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Base2>> call, Throwable t) {

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

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }
}
