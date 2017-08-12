package com.borisalexj.dogphoto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by user on 8/12/2017.
 */

public class DogDatabase  extends SQLiteOpenHelper {
    String TAG = Constants.TAG + this.getClass().getSimpleName();

    public DogDatabase(Context context) {
        super(context, DatabaseContract.DB_NAME, null, DatabaseContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: " + DatabaseContract.CREATE_DATA_TABLE_QUERY);
        db.execSQL(DatabaseContract.CREATE_DATA_TABLE_QUERY);
        db.execSQL(DatabaseContract.CREATE_DATA_VIEW_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + DatabaseContract.DATA_TABLE_NAME + ";");
        db.execSQL("drop view if exists " + DatabaseContract.DATA_VIEW_NAME + ";");
        onCreate(db);
    }

    public static final class DatabaseContract {
        public static final int DB_VERSION = 1;
        public static final String DB_NAME = "dog_db";

        public static final String DATA_TABLE_NAME = "dogs";

        public static final String CREATE_DATA_TABLE_QUERY = "create table " + DATA_TABLE_NAME + "" +
                " (" + DataColumns._ID + " integer primary key autoincrement," +
                DataColumns.PHONE_ID + " text," +
                DataColumns.PHONE_DATE + " text," +
                DataColumns.PHONE_TIME + " text," +
                DataColumns.GPS_DATE + " text," +
                DataColumns.GPS_TIME + " text," +
                DataColumns.GPS_PROVIDER + " text," +
                DataColumns.LAT + " text," +
                DataColumns.LNG + " text," +
                DataColumns.POINT_SENT + " boolean)";

        public interface DataColumns extends BaseColumns {
            String PHONE_ID = "phone_id";
            String PHONE_DATE = "phone_date";
            String PHONE_TIME = "phone_time";
            String GPS_DATE = "gps_date";
            String GPS_TIME = "gps_time";
            String GPS_PROVIDER = "gps_provider";
            String LAT = "lat";
            String LNG = "lng";
            String POINT_SENT = "point_sent";
        }
    }
}
