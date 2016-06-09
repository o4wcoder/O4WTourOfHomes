package com.fourthwardmobile.o4wtourofhomes.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicketsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketsFragment extends Fragment implements Constants{

    /***********************************************************************************/
    /*                                  Constants                                      */
    /***********************************************************************************/
    private static final String TAG = TicketsFragment.class.getSimpleName();

    public TicketsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TicketsFragment newInstance() {
        TicketsFragment fragment = new TicketsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        inflater.inflate(R.layout.fragment_tickets_available, container, false);

        //Get if tickets are available from shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean defaultValue = Boolean.parseBoolean(getString(R.string.pref_tickets_available_default));
        boolean isTicketsAvailable = prefs.getBoolean(getString(R.string.pref_tickets_available_key),defaultValue);

        if(isTicketsAvailable) {
            view = inflater.inflate(R.layout.fragment_tickets_available, container, false);

            final String ticketUrl = prefs.getString(MSG_KEY_TICKETS_URL,"");
            LinearLayout ticketLayout = (LinearLayout)view.findViewById(R.id.ticket_layout);
            ticketLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Log.e(TAG,"onClick()");

                    if(!(ticketUrl.equals(""))) {
                       // String ticketUrl = "https://www.eventbrite.com/e/fall-in-the-4th-ward-tour-of-homes-tickets-12254215689";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(ticketUrl));
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getActivity(),getString(R.string.tickets_url_error),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else
            view = inflater.inflate(R.layout.fragment_tickets, container, false);

        Log.e(TAG,"onCraeteView(): tickets_available = " + isTicketsAvailable);
        ImageView ticketImageView = (ImageView)view.findViewById(R.id.ticket_image_view);
        ticketImageView.setAlpha(0f);
        ticketImageView.animate().setDuration(1000).alpha(.05f);


        return view;
    }



}
