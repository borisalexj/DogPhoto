package com.borisalexj.dogphoto.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.borisalexj.dogphoto.R;
import com.borisalexj.dogphoto.db.DogOrm;
import com.borisalexj.dogphoto.geolocation.MyLocationListener;
import com.borisalexj.dogphoto.models.DogModel;
import com.borisalexj.dogphoto.util.Constants;
import com.borisalexj.dogphoto.util.Utils;
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
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;

public class AddDetailActivity extends AppCompatActivity {
    private String TAG = Constants.TAG + this.getClass().getSimpleName();

    private String filename;
    private EditText details_date;
    private EditText details_address;
    private EditText details_size;
    private EditText details_mast;
    private EditText details_oshiynik;
    private EditText details_name;
    private EditText details_klipsa;
    private EditText details_osoblivi_prikmety;
    private EditText primitki;
    private Location mLastLocation;
    private EditText details_poroda;
    private long mCurrentDateTime;
    private MyLocationListener mNetLocationListener = new NetLocationListener(LocationManager.NETWORK_PROVIDER);
    private MyLocationListener mGpsLocationListener = new GpsLocationListener(LocationManager.GPS_PROVIDER);
    private LocationManager mGpsLocationManager = null;
    private LocationManager mNetLocationManager = null;

    private String intentActionName = TAG + "reverse_geocoding_result";
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: inside receiver");
            if (intent.getAction().equals(intentActionName)) {
//                Toast.makeText(AddDetailActivity.this, String.valueOf(intent.getStringExtra("result")), Toast.LENGTH_SHORT).show();
                details_address.setText(String.valueOf(intent.getStringExtra("result")));

            }
        }
    };
    private boolean mGpsGood = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);

        details_date = (EditText) findViewById(R.id.add_details_date);
        details_address = (EditText) findViewById(R.id.add_details_geo);
        details_poroda = (EditText) findViewById(R.id.add_details_poroda);
        details_size = (EditText) findViewById(R.id.add_details_size);
        details_mast = (EditText) findViewById(R.id.add_details_mast);
        details_oshiynik = (EditText) findViewById(R.id.add_details_oshiynik);
        details_name = (EditText) findViewById(R.id.add_details_name);
        details_klipsa = (EditText) findViewById(R.id.add_details_klipsa);
        details_osoblivi_prikmety = (EditText) findViewById(R.id.add_details_prikmety);
        primitki = (EditText) findViewById(R.id.add_details_primitki);


        Intent incomingIntent = getIntent();
        if (incomingIntent != null) {
            filename = incomingIntent.getStringExtra("filename");
        }
        ImageView iv = (ImageView) findViewById(R.id.add_detail_image_view);

        Utils.setImageViewFromFile(iv, filename);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Введіть дані про тварину");


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
    protected void onPostResume() {
        super.onPostResume();
        makeRequestForGps();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onPostResume: permission denied");
            Log.d(TAG, "onPostResume: permission check failed");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.d(TAG, "onPostResume: should show toast");
                Toast.makeText(getApplicationContext(), "GPS permission allows us to access location data. Please allow in App PppSettings for additional functionality.", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "onPostResume: should request permission");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.Requests.REQUEST_CODE_LOCATION);

            return;
        } else {
            initializeLocationServices();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        initializeLocationServices();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(intentActionName);
        intentFilter.addAction(TAG + "geocoding_result");
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        deinitializeLocationServices();
        try {
            unregisterReceiver(mBroadcastReceiver);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillValues();

    }

    private void fillValues() {
        Calendar c = Calendar.getInstance();
        Date currentDateTime = c.getTime();
        mCurrentDateTime = currentDateTime.getTime();
        details_date.setText(Utils.getDateTimeFromLong(mCurrentDateTime, Constants.DATE_TIME_FORMAT));
        showAnimateEditText(details_date);
    }

    public void detailsDoneClick(View view) {
        addDetailsDone();
    }

    private void addDetailsDone() {
        DogModel dm = new DogModel();

        dm.setPhoto(String.valueOf(filename));
        dm.setDate(String.valueOf(details_date.getText()));
        dm.setAddress(String.valueOf(details_address.getText()));
        dm.setPoroda(String.valueOf(details_poroda.getText()));
        if (mLastLocation != null) {
            dm.setLat(String.valueOf(mLastLocation.getLatitude()));
            dm.setLng(String.valueOf(mLastLocation.getLongitude()));
        }
        dm.setSize(String.valueOf(details_size.getText()));
        dm.setMast(String.valueOf(details_mast.getText()));
        dm.setOshiynik(String.valueOf(details_oshiynik.getText()));
        dm.setName(String.valueOf(details_name.getText()));
        dm.setKlipsa(String.valueOf(details_klipsa.getText()));
        dm.setPrikmety(String.valueOf(details_osoblivi_prikmety.getText()));
        dm.setPrimitki(String.valueOf(primitki.getText()));

        (new DogOrm(this, TAG)).storeDog(dm);
        Intent intent = new Intent(this, MapsActivity.class);
        if (mLastLocation != null) {
            intent.putExtra("lat", mLastLocation.getLatitude());
            intent.putExtra("lng", mLastLocation.getLongitude());
        }
        startActivity(intent);

    }

    private void updateLocation(Location mLastLocation) {
        Log.d(TAG, "updateLocation: ");
        (findViewById(R.id.add_detail_done_button)).setEnabled(true);

        GeoApiContext context;
        context = new GeoApiContext().setApiKey(this.getResources().getString(R.string.google_geocoding_key));
        GeocodingApiRequest req = GeocodingApi.reverseGeocode(context, new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

        req.setCallback(new com.google.maps.PendingResult.Callback<GeocodingResult[]>() {
            @Override
            public void onResult(GeocodingResult[] result) {
                Log.d(TAG, "onResult: ");
                // Handle successful request.

                Log.d(TAG, "geocoding successful");
                Log.d(TAG, result[0].formattedAddress);
//                System.out.println(result[0].formattedAddress);
                Intent intent = new Intent(intentActionName);
                intent.putExtra("result", result[0].formattedAddress);
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Throwable e) {
                // Handle error.
                Log.d(TAG, "geocoding error");
            }
        });

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

    public void add_details_date_label(View view) {
        showAnimateEditText((EditText) findViewById(R.id.add_details_date));
    }

    private void showAnimateEditText(EditText et) {
        et.setVisibility(View.VISIBLE);
        Animation searchShowAnimation;
        searchShowAnimation = AnimationUtils.loadAnimation(this, R.anim.show_edit_text);
        searchShowAnimation.setRepeatCount(1);
        et.startAnimation(searchShowAnimation);

    }

    public void add_details_geo_label(View view) {
        showAnimateEditText((EditText) findViewById(R.id.add_details_geo));
    }

    public void add_details_poroda_label(View view) {
        showAnimateEditText((EditText) findViewById(R.id.add_details_poroda));
    }

    public void add_details_size_label(View view) {
        showAnimateEditText((EditText) findViewById(R.id.add_details_size));

    }

    public void add_details_mast_label(View view) {
        showAnimateEditText((EditText) findViewById(R.id.add_details_mast));

    }

    public void add_details_oshiynik_label(View view) {
        showAnimateEditText((EditText) findViewById(R.id.add_details_oshiynik));

    }

    public void add_details_name_label(View view) {
        showAnimateEditText((EditText) findViewById(R.id.add_details_name));

    }

    public void add_details_klipsa_label(View view) {
        showAnimateEditText((EditText) findViewById(R.id.add_details_klipsa));

    }

    public void add_details_prikmety_label(View view) {
        showAnimateEditText((EditText) findViewById(R.id.add_details_prikmety));

    }

    public void add_details_primitky_label(View view) {
        showAnimateEditText((EditText) findViewById(R.id.add_details_primitki));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        switch (requestCode) {
            case Constants.Requests.REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: granted");
                    initializeLocationServices();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: NOT granted");
                    Toast.makeText(AddDetailActivity.this, "Permission Denied, You cannot access location data.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_detail_done) {
            addDetailsDone();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            if (mLastLocation == null) {
                Toast.makeText(this, "Потрібно включити геолокацію", Toast.LENGTH_SHORT).show();

            } else {

                finish();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private class NetLocationListener extends MyLocationListener {
        public NetLocationListener(String provider) {
            super(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location + " gpsDateTime:" + location.getTime());
            mLastLocation.set(location);
            AddDetailActivity.this.mLastLocation = mLastLocation;
//            Toast.makeText(AddDetailActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
            showAnimateEditText((EditText) findViewById(R.id.add_details_geo));
            details_address.setText(mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
            updateLocation(mLastLocation);
        }
    }

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
            AddDetailActivity.this.mLastLocation = mLastLocation;
//            Toast.makeText(AddDetailActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
            showAnimateEditText((EditText) findViewById(R.id.add_details_geo));
            details_address.setText(mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
            updateLocation(mLastLocation);
        }
    }
}
