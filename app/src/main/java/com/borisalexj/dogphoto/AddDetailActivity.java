package com.borisalexj.dogphoto;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.File;

public class AddDetailActivity extends AppCompatActivity {
    private String TAG = Info.TAG + this.getClass().getSimpleName();

    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);

        Intent incomingIntent = getIntent();
        if (incomingIntent != null) {
            filename = incomingIntent.getStringExtra("filename");
        }
        ImageView iv = (ImageView) findViewById(R.id.detail_image_view);

        if (!TextUtils.isEmpty(filename)) {
            File imgFile = new File(filename);

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                iv.setImageBitmap(myBitmap);

            }
        }
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeLocationServices();

    }

    @Override
    protected void onStop() {
        super.onStop();
        deinitializeLocationServices();
    }

    private MyLocationListener mNetLocationListener = new NetLocationListener(LocationManager.NETWORK_PROVIDER);
    private MyLocationListener mGpsLocationListener = new GpsLocationListener(LocationManager.GPS_PROVIDER);
    private LocationManager mGpsLocationManager = null;
    private LocationManager mNetLocationManager = null;

    private class NetLocationListener extends MyLocationListener {
        public NetLocationListener(String provider) {
            super(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location + " gpsDateTime:" + location.getTime());
            mLastLocation.set(location);
            Toast.makeText(AddDetailActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
            // Todo - UpdateLocation
        }
    }

    public void makeRequestForGps() {
        Log.d(TAG, "makeRequestForGps: ");
        GoogleApiClient googleApiClient;

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        locationRequest.setInterval(Constants.LOCATION_INTERVAL);
        locationRequest.setFastestInterval(Math.round(Constants.LOCATION_INTERVAL) / 2);
        locationRequest.setExpirationTime(Constants.LOCATION_INTERVAL);
        locationRequest.setExpirationDuration(Constants.LOCATION_INTERVAL);
        locationRequest.setSmallestDisplacement(Constants.LOCATION_DISTANCE);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); // this is the key ingredient

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(
                new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(@NonNull LocationSettingsResult result) {

                        final Status status = result.getStatus();
                        final LocationSettingsStates state = result.getLocationSettingsStates();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
//                                    setGpsStatusGreen();
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    status.startResolutionForResult(AddDetailActivity.this, Constants.Requests.REQUEST_LOCATION);
                                } catch (IntentSender.SendIntentException e) {
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                break;

                        }
                    }
                }
        );

//        googleApiClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        googleApiClient.disconnect();
    }

    private boolean mGpsGood = false;

    private class GpsLocationListener extends MyLocationListener {
        public GpsLocationListener(String provider) {
            super(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            super.onProviderDisabled(provider);
            mGpsGood = false;
            makeRequestForGps();
        }

        @Override
        public void onProviderEnabled(String provider) {
            super.onProviderEnabled(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location + " gpsDateTime:" + location.getTime());
            mLastLocation.set(location);
            Toast.makeText(AddDetailActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
            // ToDO Update Location
        }
    }


    private void initializeLocationManager() {
        Log.i(TAG, "initializeLocationManager");
        if (mGpsLocationManager == null) {
            mGpsLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        if (mNetLocationManager == null) {
            mNetLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void initializeLocationServices() {
        initializeLocationManager();
        try {
            mNetLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, Constants.LOCATION_INTERVAL, Constants.LOCATION_DISTANCE,
                    mNetLocationListener);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mGpsLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, Constants.LOCATION_INTERVAL, Constants.LOCATION_DISTANCE,
                    mGpsLocationListener);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    public void deinitializeLocationServices() {

        if (mNetLocationManager != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mNetLocationManager.removeUpdates(mNetLocationListener);
            } catch (Exception ex) {
                Log.i(TAG, "fail to remove location listeners, ignore", ex);
            }
        }

        if (mGpsLocationManager != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mGpsLocationManager.removeUpdates(mGpsLocationListener);
            } catch (Exception ex) {
                Log.i(TAG, "fail to remove location listeners, ignore", ex);
            }
        }
    }




}