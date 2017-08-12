package com.borisalexj.dogphoto.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.borisalexj.dogphoto.util.Constants;

/**
 * Created by user on 8/12/2017.
 */

public class DogDatabase extends SQLiteOpenHelper {
    String TAG = Constants.TAG + this.getClass().getSimpleName();

    public DogDatabase(Context context) {
        super(context, DatabaseContract.DB_NAME, null, DatabaseContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: " + DatabaseContract.CREATE_DATA_TABLE_QUERY);
        db.execSQL(DatabaseContract.CREATE_DATA_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + DatabaseContract.DATA_TABLE_NAME + ";");
        onCreate(db);
    }

    public static final class DatabaseContract {
        public static final int DB_VERSION = 4;
        public static final String DB_NAME = "dog_db";

        public static final String DATA_TABLE_NAME = "dogs";

        public static final String CREATE_DATA_TABLE_QUERY = "create table " + DATA_TABLE_NAME + "" +
                " (" + DataColumns._ID + " integer primary key autoincrement," +
                DataColumns.PHOTO + " text," +
                DataColumns.DATE + " text," +
                DataColumns.ADRRESS + " text," +
                DataColumns.PORODA + " text," +
                DataColumns.LAT + " text," +
                DataColumns.LNG + " text," +
                DataColumns.SIZE + " text," +
                DataColumns.MAST + " text," +
                DataColumns.OSHIYNIK + " text," +
                DataColumns.NAME + " text," +
                DataColumns.KLIPSA + " text," +
                DataColumns.PRIKMETY + " text," +
                DataColumns.PRIMITKI + " text)";

        public interface DataColumns extends BaseColumns {
            String PHOTO = "photo";
            String DATE = "date";
            String ADRRESS = "address";
            String PORODA = "poroda";
            String LAT = "lat";
            String LNG = "lng";
            String SIZE = "size";
            String MAST = "mast";
            String OSHIYNIK = "oshiynik";
            String NAME = "name";
            String KLIPSA = "klipsa";
            String PRIKMETY = "prikmety";
            String PRIMITKI = "primitki";
        }
    }
}
