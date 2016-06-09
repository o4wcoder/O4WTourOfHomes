package com.fourthwardmobile.o4wtourofhomes.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.activities.MainActivity;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class TourFirebaseMessagingService extends FirebaseMessagingService implements Constants {

    /**************************************************************************************/
    /*                                Local Data                                          */
    /**************************************************************************************/
    private static final String TAG = TourFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG,"onMessageReceived() From: " + remoteMessage.getFrom());
        Log.e(TAG,"onMessageReceived() Message Body = " + remoteMessage.getNotification().getBody());
        Log.e(TAG,"onMessageReceived() Data = " + remoteMessage.getData());

        if(remoteMessage.getData().containsKey(MSG_KEY_TICKETS_AVAILABLE)) {


            Log.e(TAG,"Got tickets_avaialble key");

            Log.e(TAG,"Got ticket_available value = " + remoteMessage.getData().get(MSG_KEY_TICKETS_AVAILABLE));


            String ticketValue = remoteMessage.getData().get(MSG_KEY_TICKETS_AVAILABLE);
            Log.e(TAG, "got ticket value = " + ticketValue);
            //Save ticket availablity to shared preferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(getString(R.string.pref_tickets_available_key), Boolean.parseBoolean(ticketValue));

            //Get URL for the site to purchase tickets
            if(remoteMessage.getData().containsKey(MSG_KEY_TICKETS_URL)) {
                String ticketURL = remoteMessage.getData().get(MSG_KEY_TICKETS_URL);
                editor.putString(MSG_KEY_TICKETS_URL,ticketURL);
            }
            editor.commit();
            sendNotification(remoteMessage.getNotification().getBody());
        }


    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
