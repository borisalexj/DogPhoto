package com.borisalexj.dogphoto.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.borisalexj.dogphoto.R;
import com.borisalexj.dogphoto.util.Constants;

public class StartActivity extends AppCompatActivity {
    String TAG = Constants.TAG + this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    private void startCameraActivity() {
        startActivity(new Intent(this, CameraActivity.class));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        askPermissionForCamera();

    }

    private void askPermissionForCamera() {
        Log.d(TAG, "askPermissionForCamera: ");
        if (Build.VERSION.SDK_INT >= 23) {
// Here, thisActivity is the current activity
            if ((ContextCompat.checkSelfPermission(StartActivity.this, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) || (
                    (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)
            )) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(StartActivity.this,
                        android.Manifest.permission.CAMERA)
                        ) {
                    Toast.makeText(getApplicationContext(), "Accessing a camera allows us to use a camera for check-in. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {
                    // No explanation needed, we can request the permission.

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
                ActivityCompat.requestPermissions(StartActivity.this,
                        new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.Requests.REQUEST_FOR_CAMERA);
            } else {
                Log.d(TAG, "askPermissionForCamera: 1");
                startCameraActivity();
            }
        } else {
            Log.d(TAG, "askPermissionForCamera: 2");
            startCameraActivity();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");

        if (requestCode == Constants.Requests.REQUEST_FOR_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                // Todo
//                openGalleryToChoosePicture(requestCode);
            } else {
                // Todo
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }


        if (requestCode == Constants.Requests.REQUEST_FOR_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                Log.d(TAG, "askPermissionForCamera: 3");

                startCameraActivity();
            } else {
                finish();
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }


}
