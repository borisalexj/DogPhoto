package com.borisalexj.dogphoto;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by user on 8/12/2017.
 */

public class DogOrm {
    String TAG = Constants.TAG + this.getClass().getSimpleName();
    Context mContext;

    public DogOrm(Context context, String tag) {
        this.mContext = context;
        DogDatabase dogDb = new DogDatabase(context);
        TAG = tag + " " + this.getClass().getSimpleName();
    }

    public void storeDog(DogModel dm) {
        Log.d(TAG, "storeLocation: ");
        DogDatabase dogDb = new DogDatabase(mContext);
        SQLiteDatabase sqLiteDogDb = dogDb.getWritableDatabase();
        try {
            sqLiteDogDb.insert(DogDatabase.DatabaseContract.DATA_TABLE_NAME, null, dm.toDb());
        } catch (Exception e) {
            Log.d(TAG, "storeLocation: storing failed");
            e.printStackTrace();
        } finally {
            sqLiteDogDb.close();
            dogDb.close();
        }
    }

    public ArrayList<DogModel> getPointsFromDb() {
        ArrayList<DogModel> dogsArray = new ArrayList<>();
        Log.d(TAG, "getPointsFromDb: ");
        DogDatabase pppDb = new DogDatabase(mContext);
        SQLiteDatabase sqLitePppDb = pppDb.getReadableDatabase();

        Cursor cursor = sqLitePppDb.query(DogDatabase.DatabaseContract.DATA_TABLE_NAME,
                null, null, null, null, null, DogDatabase.DatabaseContract.DataColumns._ID + " DESC");
        Log.d(TAG, "getPointsFromDb: cursor_size - " + String.valueOf(cursor.getCount()));
        cursor.moveToFirst();
        dogsArray.clear();
        if (cursor.getCount() != 0) {
            do {
                dogsArray.add(new DogModel(
                        cursor.getInt(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns._ID)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.PHOTO)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.DATE)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.ADRRESS)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.LAT)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.LNG)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.SIZE)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.MAST)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.OSHIYNIK)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.NAME)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.KLIPSA)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.PRIKMETY)),
                        cursor.getString(cursor.getColumnIndex(DogDatabase.DatabaseContract.DataColumns.PRIMITKI))
                ));
            } while (cursor.moveToNext());
            Log.d(TAG, "getPointsFromDb: array_size - " + String.valueOf(dogsArray.size()));
        }
        sqLitePppDb.close();
        pppDb.close();
        return dogsArray;
    }


}