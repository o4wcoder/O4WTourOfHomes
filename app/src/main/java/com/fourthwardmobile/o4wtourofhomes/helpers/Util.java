package com.fourthwardmobile.o4wtourofhomes.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

    /**
     * Detect is there is a network connection
     * @param context Context testing connectivity
     * @return        Return status of network connectivity
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
