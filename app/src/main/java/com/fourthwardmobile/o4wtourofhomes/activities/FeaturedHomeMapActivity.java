package com.fourthwardmobile.o4wtourofhomes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
import com.google.android.gms.maps.model.LatLng;

public class FeaturedHomeMapActivity extends AppCompatActivity implements Constants{

    /*******************************************************************/
    /*                            Constants                            */
    /*******************************************************************/
    private static final String TAG = FeaturedHomeMapActivity.class.getSimpleName();

    /*******************************************************************/
    /*                           Local Data                            */
    /*******************************************************************/
    LatLng mZoomLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        Intent intent = getIntent();

        mZoomLocation = intent.getParcelableExtra(EXTRA_LOCATION);


        if(mZoomLocation != null) {

            Log.e(TAG,"Got location = " + mZoomLocation.toString());
        }


    }
}
