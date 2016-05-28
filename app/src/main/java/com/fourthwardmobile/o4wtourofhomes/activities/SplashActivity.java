package com.fourthwardmobile.o4wtourofhomes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;

public class SplashActivity extends AppCompatActivity implements Constants {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Check for intent coming from Firebase Cloud Messaging Notification
        if(getIntent().getExtras() != null) {
            Log.e(TAG,"onCraete(): Got some extras. Let's see if we have any Firebase data!!!");
            Log.e(TAG,"onCreate(): Got ticket = " + getIntent().getExtras().getString(MSG_KEY_TICKETS_AVAILABLE));

            if(getIntent().getExtras().containsKey(MSG_KEY_TICKETS_AVAILABLE)) {

                String ticketValue = getIntent().getExtras().getString(MSG_KEY_TICKETS_AVAILABLE);
                Log.e(TAG,"got ticket value = " + ticketValue);
                //Save ticket availablity to shared preferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(getString(R.string.pref_tickets_available_key),Boolean.parseBoolean(ticketValue));
                editor.commit();
            }
        }
        else {
            Log.e(TAG,"onCreate(): Extra in MainActivity were null. No Firebase data!!!!!!");
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
