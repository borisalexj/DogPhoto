package com.borisalexj.dogphoto;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.Image;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String TAG = Info.TAG + this.getClass().getSimpleName();

    SurfaceView preview;
    Camera camera;
    MediaRecorder mediaRecorder;

    File photoFile;
    File videoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File pictures = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        photoFile = new File(pictures, imageFileName + ".jpg");
        videoFile = new File(pictures, "myvideo.3gp");

        preview = (SurfaceView) findViewById(R.id.photo_preview_surface);

        SurfaceHolder holder = preview.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG, "surfaceCreated: ");
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();

                    ViewGroup.LayoutParams params = preview.getLayoutParams();

                    Camera.Parameters parameters = camera.getParameters();
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
                    preview.setLayoutParams(params);

//            codeImage = new Image(379, 379, "Y800");
//                    codeImage = new Image(size.width, size.height, "Y800");
//                    previewing = true;
                    preview.refreshDrawableState();

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
        super.onResume();

        askPermissionForCamera();
        askPermissionForStorage();
    }

    private void askPermissionForStorage() {
        if (Build.VERSION.SDK_INT >= 23) {
// Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
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
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.Requests.REQUEST_FOR_STORAGE);
            } else {
//                openGalleryToChoosePicture(galleryRequest);
            }
        } else {
//            openGalleryToChoosePicture(galleryRequest);
        }
    }


    private void askPermissionForCamera(){

        if (Build.VERSION.SDK_INT >= 23) {
// Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
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
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        Constants.Requests.REQUEST_FOR_CAMERA);
            } else {
                resumeCamera();
            }
        } else {
            resumeCamera();
        }

    }

    private void resumeCamera() {
        camera = Camera.open();

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
                resumeCamera();
            } else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null)
            camera.release();
        camera = null;
    }

    public void onClickPicture(View view) {
        Log.d(TAG, "onClickPicture: ");
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d(TAG, "onPictureTaken: ");
                try {
                    FileOutputStream fos = new FileOutputStream(photoFile);
                    fos.write(data);
                    fos.close();
                    Intent intent = new Intent(MainActivity.this, AddDetailActivity.class);
                    intent.putExtra("filename", Utils.transformImage(MainActivity.this,photoFile.getCanonicalPath(), 1024, 1024));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void goToListClick(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }
}
