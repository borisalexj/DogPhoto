package com.borisalexj.dogphoto;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    String TAG = Constants.TAG + this.getClass().getSimpleName();

    private GoogleMap mMap;
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private DogsRecyclerViewAdapter mAdapter;
    private ArrayList<DogModel> mDogsList;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent incomingIntent = getIntent();
        if (incomingIntent != null) {
            latLng = new LatLng(
                    incomingIntent.getDoubleExtra("lat", 0),
                    incomingIntent.getDoubleExtra("lng", 0));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mDogsList = (new DogOrm(this, TAG)).getPointsFromDb();
        mAdapter = new DogsRecyclerViewAdapter(this, mDogsList);

//        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        mRecyclerView.setAdapter(mAdapter);


        Log.d(TAG, "onCreate: " + (new DogOrm(this, TAG)).getPointsFromDb());

    }

    public void addMarkerToMap(Double lat, Double lng) {
        LatLng latLng = new LatLng(lat, lng);
        Log.d(TAG, "AddMarkersToMap: ");
        try {
            mMap.clear();
        } catch (NullPointerException e) {
            Log.d(TAG, "AddMarkersToMap: problem with google maps - map isn't initialized");
            return;
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.INITIAL_MAP_ZOOM_LEVEL));
//                .title(venue.getName())
//                .snippet(String.valueOf(venue.getDistance()));

        mMap.addMarker(markerOptions);//.setIcon((BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(markerDrawable))));
//            BitmapDescriptor markerIcon = BitmapDescriptorFactory.defaultMarker(venue.isHighestRate() ? BitmapDescriptorFactory.HUE_RED : BitmapDescriptorFactory.HUE_GREEN);
//            mMap.addMarker(markerOptions).setIcon((markerIcon));

//        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (latLng == null) {
            latLng = new LatLng(-34, 151);
        }

//        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.INITIAL_MAP_ZOOM_LEVEL));

    }
}
