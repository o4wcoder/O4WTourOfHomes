package com.fourthwardmobile.o4wtourofhomes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);

        //Get if tickets are available from shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean defaultValue = Boolean.parseBoolean(getString(R.string.pref_tickets_available_default));
        boolean isTicketsAvailable = prefs.getBoolean(getString(R.string.pref_tickets_available_key),defaultValue);

        Log.e(TAG,"onCraeteView(): tickets_available = " + isTicketsAvailable);
        ImageView ticketImageView = (ImageView)view.findViewById(R.id.ticket_image_view);
        ticketImageView.setAlpha(0f);
        ticketImageView.animate().setDuration(1000).alpha(.05f);


        return view;
    }


}
