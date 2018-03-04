package com.online.foodplus.dialogs;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.online.foodplus.R;
import com.online.foodplus.utils.Tool;

/**
 * Created by 1918 on 02-Feb-17.
 */

public class MapDialog extends DialogFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private int DEFAULT_MARKER_SIZE;
    private static View rootView;
    private static String title, description;
    private static Double longitude, latitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.dialog_map_simple, container, false);
        } catch (InflateException e) {
                /* map is already there, just return view as it is */
        }
        //        View rootView = inflater.inflate(R.layout.dialog_map_simple, container, false);
        setUpDialog();
        init(rootView);
        return rootView;
    }

    public static MapDialog newInstance(String mtitle, String mDescription, Double mLatitude, Double mLongitude) {
        title = mtitle;
        description = mDescription;
        longitude = mLongitude;
        latitude = mLatitude;
        return new MapDialog();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window != null) {
            dialog.setCanceledOnTouchOutside(true);
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            Double d = (screenWidth * 1.3);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, d.intValue());//dialog height = screen width
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void setUpDialog() {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        dialog.setCancelable(true);
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setDimAmount(0.3f);
        }
    }

    private void init(View view) {
        if (view == null) return;
        DEFAULT_MARKER_SIZE = Tool.getScreenWidth(getActivity()) / 12;

        TextView titleTextView = (TextView) view.findViewById(R.id.map_title);
        titleTextView.setText(title);
        TextView addressTextView = (TextView) view.findViewById(R.id.map_address);
        addressTextView.setText(description);
        ImageView imgClose = (ImageView) view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        SupportMapFragment mapFragment =
                (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(latitude, longitude);
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle_retro));
        MarkerOptions markerOptions = new MarkerOptions().position(location);
        markerOptions.icon(BitmapDescriptorFactory
                .fromBitmap(resizeMapIcons(R.drawable.ic_marker, DEFAULT_MARKER_SIZE, DEFAULT_MARKER_SIZE)));
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
    }

    private Bitmap resizeMapIcons(int icon, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), icon);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
}
