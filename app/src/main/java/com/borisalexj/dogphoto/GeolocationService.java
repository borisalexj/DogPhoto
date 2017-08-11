package com.borisalexj.dogphoto;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by user on 8/11/2017.
 */

public class GeolocationService extends Service {
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    private String TAG = Info.TAG + this.getClass().getSimpleName() + " " + this.hashCode() + " ";
    private LocationListener mNetLocationListener = new GeolocationService.NetLocationListener(LocationManager.NETWORK_PROVIDER);
    private LocationListener mGpsLocationListener = new GeolocationService.GpsLocationListener(LocationManager.GPS_PROVIDER);
    private LocationManager mGpsLocationManager = null;
    private LocationManager mNetLocationManager = null;
    private boolean mGpsGood = false;
    private Location mLastLocation;
    private boolean mLocationServisesInitialized = false;

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deinitializeLocationServices();
    }

    public void initializeLocationServices() {
        Log.d(TAG, "initializeLocationServices: ");
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

    private void sendLocation(Location location) {
        Intent intent = new Intent(Constants.INTENT_GOT_LOCATION);
        intent.putExtra("location", location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void deinitializeLocationServices() {
        Log.d(TAG, "deinitializeLocationServices: ");
//        mGoogleApiClient.disconnect();

        if (mNetLocationManager != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mNetLocationManager.removeUpdates(mNetLocationListener);
            } catch (Exception ex) {
                Log.i(TAG, "fail to remove location listners, ignore", ex);
            }
        }

        if (mGpsLocationManager != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mGpsLocationManager.removeUpdates(mGpsLocationListener);
            } catch (Exception ex) {
                Log.i(TAG, "fail to remove location listners, ignore", ex);
            }
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

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: ");
            switch (msg.what) {
                case Constants.MSG_START_GEOLOCATION:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    if (!mLocationServisesInitialized) {
                        initializeLocationServices();
                    }
                    mLocationServisesInitialized = true;
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public GeolocationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return GeolocationService.this;
        }
    }

    private class NetLocationListener extends GeolocationService.LocationListener {
        public NetLocationListener(String provider) {
            super(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location + " gpsDateTime:" + location.getTime());
            if (!mGpsGood) {
                mLastLocation.set(location);
                sendLocation(mLastLocation);
            }
            Toast.makeText(GeolocationService.this, "net\nlat:" + mLastLocation.getLatitude() + "\nlng:" + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }

    private class GpsLocationListener extends GeolocationService.LocationListener {
        public GpsLocationListener(String provider) {
            super(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            super.onProviderDisabled(provider);
//            makeRequestForGpsTurnedOn();
            mGpsGood = false;
        }

        @Override
        public void onProviderEnabled(String provider) {
            super.onProviderEnabled(provider);
            mGpsGood = true;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location + " gpsDateTime:" + location.getTime());
            mLastLocation.set(location);
            sendLocation(mLastLocation);
            mGpsGood = true;
            Toast.makeText(GeolocationService.this, "gps\nlat:" + mLastLocation.getLatitude() + "\nlng:" + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }

    private abstract class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            Log.i(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: " + provider);
        }
    }




}
