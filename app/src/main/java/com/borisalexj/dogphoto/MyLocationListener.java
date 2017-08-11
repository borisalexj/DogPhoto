package com.borisalexj.dogphoto;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by user on 8/11/2017.
 */

public abstract class MyLocationListener implements android.location.LocationListener {
    private final String TAG = Constants.TAG + this.getClass().getSimpleName();
    protected Location mLastLocation;

    public MyLocationListener(String provider) {
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
