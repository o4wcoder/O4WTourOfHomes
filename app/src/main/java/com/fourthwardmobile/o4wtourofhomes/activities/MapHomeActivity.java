package com.fourthwardmobile.o4wtourofhomes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.fragments.MapHomeFragment;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
import com.fourthwardmobile.o4wtourofhomes.models.Home;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapHomeActivity extends AppCompatActivity implements Constants{

    /*************************************************************************************/
    /*                                  Constants                                        */
    /*************************************************************************************/
    private static final String TAG = MapHomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_home);

        //Get data passed by activity calling Map outside of the main activity drawer.
        Intent intent = getIntent();


        if(intent != null) {
            Bundle bundle = new Bundle();
            ArrayList<Home> list = intent.getParcelableArrayListExtra(EXTRA_HOME_LIST);
            LatLng location = intent.getParcelableExtra(EXTRA_LOCATION);
            Log.e(TAG,"onCreate with location = " + location.toString());

            MapHomeFragment fragment = MapHomeFragment.newInstance(list,location);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_single_home_container,fragment).commit();
        }
    }

}
