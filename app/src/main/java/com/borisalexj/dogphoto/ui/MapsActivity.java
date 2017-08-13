package com.borisalexj.dogphoto.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.borisalexj.dogphoto.R;
import com.borisalexj.dogphoto.db.DogOrm;
import com.borisalexj.dogphoto.models.DogModel;
import com.borisalexj.dogphoto.ui.adapters.DogsRecyclerViewAdapter;
import com.borisalexj.dogphoto.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    String TAG = Constants.TAG + this.getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private LatLng mLatLng;
    private GoogleMap mMap;
    private LinearLayoutManager mLinearLayoutManager;
    private DogsRecyclerViewAdapter mAdapter;
    private ArrayList<DogModel> mDogsList;

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
            mLatLng = new LatLng(
                    incomingIntent.getDoubleExtra("lat", 0),
                    incomingIntent.getDoubleExtra("lng", 0));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.map_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_home_white_24dp);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Карта");


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mDogsList = (new DogOrm(this, TAG)).getDogsFrom();
        mAdapter = new DogsRecyclerViewAdapter(this, mDogsList);

//        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        mRecyclerView.setAdapter(mAdapter);


        Log.d(TAG, "onCreate: " + (new DogOrm(this, TAG)).getDogsFrom());

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
        if (mLatLng == null) {
            mLatLng = new LatLng(-34, 151);
        }

//        mMap.addMarker(new MarkerOptions().position(mLatLng).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, Constants.INITIAL_MAP_ZOOM_LEVEL));

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_map_search) {
            Toast.makeText(this, "Will be implemented", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
