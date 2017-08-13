package com.borisalexj.dogphoto.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.borisalexj.dogphoto.R;
import com.borisalexj.dogphoto.util.Constants;
import com.borisalexj.dogphoto.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    private String TAG = Constants.TAG + this.getClass().getSimpleName();

    private SurfaceView mPreview;
    private Camera mCamera;
    private File mPhotoFile;
    private SurfaceHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        File pictures = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        mPhotoFile = new File(pictures, imageFileName + ".jpg");
        mPreview = (SurfaceView) findViewById(R.id.photo_preview_surface);


    }

    private void showCameraPreview() {
        Log.d(TAG, "showCameraPreview: ");
        mHolder = mPreview.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG, "surfaceCreated: ");
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();

                    ViewGroup.LayoutParams params = mPreview.getLayoutParams();

                    Camera.Parameters parameters = mCamera.getParameters();
//            parameters.setPreviewSize(256,256);
//            mCamera.setParameters(parameters);
                    Camera.Size size = parameters.getPreviewSize();

                    float k = (float) size.width / (float) size.height;

                    Display display = getWindowManager().getDefaultDisplay();
                    DisplayMetrics outMetrics = new DisplayMetrics();
                    display.getMetrics(outMetrics);

                    float density = getResources().getDisplayMetrics().density;
                    float dpWidth = outMetrics.widthPixels;// / density;
                    float dpHeight = outMetrics.heightPixels;// / density;

//            Log.d(TAG, "onCreate: density  " + String.valueOf(density));
//            Log.d(TAG, "onCreate: dpWidth  " + String.valueOf(dpWidth));
//            Log.d(TAG, "onCreate: dpHeight " + String.valueOf(dpHeight));
//            Log.d(TAG, "onCreate: k        " + String.valueOf(k));
//            Log.d(TAG, "onCreate: /        " + String.valueOf(dpWidth/(float) k));

//            if (size.width < size.height){
//                Log.d(TAG, "resumeCamera: 1");
//            params.width=Integer.valueOf(Math.round(dpWidth));
//            params.height=Integer.valueOf(Math.round(dpWidth/(float) k));
//            } else {
//                Log.d(TAG, "resumeCamera: 2");
//                params.width=Integer.valueOf(Math.round(dpWidth));
//                params.height=Integer.valueOf(Math.round(dpHeight));
//            }

                    try {
                        params.width = Integer.valueOf(Math.round(dpWidth));
                        params.height = Integer.valueOf(Math.round(dpWidth * k));
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "resumeCamera: ");
                    }
//
                    mPreview.setLayoutParams(params);

//            codeImage = new Image(379, 379, "Y800");
//                    codeImage = new Image(size.width, size.height, "Y800");
//                    previewing = true;
                    mPreview.refreshDrawableState();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });


    }

    public void makePhotoClick(View view) {
        Log.d(TAG, "makePhotoClick: ");
        onClickPicture(view);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
//        askPermissionForStorage();

    }

    @Override
    protected void onPostResume() {
        Log.d(TAG, "onPostResume: ");
        super.onPostResume();
        askPermissionForCamera();

    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    private void askPermissionForStorage() {
        Log.d(TAG, "askPermissionForStorage: ");
        if (Build.VERSION.SDK_INT >= 23) {
// Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(CameraActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(CameraActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(getApplicationContext(), "Accessing an external storage allows us to choose photo. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                } else {
                    // No explanation needed, we can request the permission.

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.Requests.REQUEST_FOR_STORAGE);
            } else {
//                openGalleryToChoosePicture(galleryRequest);
            }
        } else {
//            openGalleryToChoosePicture(galleryRequest);
        }
    }


    private void askPermissionForCamera() {
        Log.d(TAG, "askPermissionForCamera: ");
        if (Build.VERSION.SDK_INT >= 23) {
// Here, thisActivity is the current activity
            if ((ContextCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) || (
                    (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)
            )) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(CameraActivity.this,
                        android.Manifest.permission.CAMERA)
                        ) {
                    Toast.makeText(getApplicationContext(), "Accessing a mCamera allows us to use a mCamera for check-in. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {
                    // No explanation needed, we can request the permission.

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.Requests.REQUEST_FOR_CAMERA);
            } else {
                Log.d(TAG, "askPermissionForCamera: 1");
                resumeCamera();
                showCameraPreview();
            }
        } else {
            Log.d(TAG, "askPermissionForCamera: 2");
            resumeCamera();
            showCameraPreview();
        }

    }

    private void resumeCamera() {
        Log.d(TAG, "resumeCamera: ");
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");

        if (requestCode == Constants.Requests.REQUEST_FOR_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use mCamera
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
                // Now user should be able to use mCamera
                Log.d(TAG, "askPermissionForCamera: 3");

//                resumeCamera();
//                showCameraPreview();
//                resumeCamera();
//                showCameraPreview();
            } else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }


    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        if (mCamera != null)
            mCamera.release();
        mCamera = null;
    }

    public void onClickPicture(View view) {
        Log.d(TAG, "onClickPicture: ");
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d(TAG, "onPictureTaken: ");
                try {
                    FileOutputStream fos = new FileOutputStream(mPhotoFile);
                    fos.write(data);
                    fos.close();
                    Intent intent = new Intent(CameraActivity.this, AddDetailActivity.class);
                    intent.putExtra("filename", Utils.transformImage(CameraActivity.this, mPhotoFile.getCanonicalPath(), 1024, 1024));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void goToListClick(View view) {
        Log.d(TAG, "goToListClick: ");
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        exit();
    }

    private void exit() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

}
