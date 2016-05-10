package com.fourthwardmobile.o4wtourofhomes.helpers;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Chris Hare on 5/9/2016.
 */
public class Util {

    public static LatLng getFourthWardParkLocation() {

        double latitude = 33.767840;
        double longitude = -84.365081;

        return new LatLng(latitude,longitude);
    }
}
