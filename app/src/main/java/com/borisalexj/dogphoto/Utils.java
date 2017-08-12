package com.borisalexj.dogphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 8/12/2017.
 */

public class Utils {
    static String TAG = Info.TAG + "Helpers";

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    public static String transformImage(AppCompatActivity activity, String inFileName, int newImgWidth, int newImgHeight) {
        Log.d(TAG, "transformImage: " + inFileName);

        File inFile = new File(inFileName);
        Bitmap bf;
        bf = BitmapFactory.decodeFile(inFileName);
        try {
        } catch (OutOfMemoryError e) {
//            return bitmap;
        }
        if (bf == null) {
            BitmapFactory.Options options;
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            bf = BitmapFactory.decodeFile(inFileName, options);
        }
        Bitmap out_bmp = ThumbnailUtils.extractThumbnail(bf, newImgWidth, newImgHeight);
//        Bitmap out_bmp = MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver‌​(), Long.parseLong(_imageUri.getLastPathSegment()), type, null);

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(inFileName);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }

        if (ei != null) {
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_UNDEFINED:
                    out_bmp = rotateImage(out_bmp, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    out_bmp = rotateImage(out_bmp, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    out_bmp = rotateImage(out_bmp, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    out_bmp = rotateImage(out_bmp, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    break;
            }
        }

        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File outFile = inFile;
        try {
            outFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            OutputStream os = new BufferedOutputStream(new FileOutputStream(outFile));
            out_bmp.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "transformImage: FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "transformImage: IOException");
        }

        return outFile.getAbsolutePath();
    }
}
