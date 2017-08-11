package com.borisalexj.dogphoto;

/**
 * Created by user on 8/11/2017.
 */

class Constants {
    public static final int RADIUS = 3000;
    public static final int START_ITEM_COUNT = 10;

    public static final int INITIAL_MAP_ZOOM_LEVEL = 14;
    public static final int LOCATION_INTERVAL = 60;
    public static final int LOCATION_DISTANCE = 50;

    public static final int MSG_START_GEOLOCATION = 1;
    public static final String INTENT_GOT_LOCATION = "INTENT_GOT_LOCATION";

    public static final String TAG = "_dog_photo_" ;

    public static class Requests {
        public static final int REQUEST_CODE_LOCATION = 11;
        public static final int REQUEST_LOCATION = 22;
        public static final int REQUEST_FOR_CAMERA = 55;
        public static final int REQUEST_FOR_STORAGE = 44;
    }

}