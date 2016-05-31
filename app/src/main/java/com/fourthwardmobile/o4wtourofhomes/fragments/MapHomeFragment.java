package com.fourthwardmobile.o4wtourofhomes.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.activities.FeaturedHomeDetailActivity;
import com.fourthwardmobile.o4wtourofhomes.helpers.Util;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
import com.fourthwardmobile.o4wtourofhomes.models.Home;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapHomeFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLoadedCallback, GoogleMap.OnInfoWindowClickListener,
        Constants {

    /**************************************************************************/
    /*                             Constants                                  */
    /**************************************************************************/
    private static final String TAG = MapHomeFragment.class.getSimpleName();

    private static final String ARG_HOME_LIST = "home_list";
    private static final String ARG_LOCATION = "location";

    /**************************************************************************/
    /*                            Local Data                                  */
    /**************************************************************************/
    private LatLng mZoomLocation;
    private ArrayList<Home> mHomeList;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private TextView mAddressTextView;
    private LinearLayout mAddressPanel;


    // private OnFragmentInteractionListener mListener;

    public MapHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapHomeFragment newInstance(ArrayList<Home> homeList, LatLng zoomLocation) {
        MapHomeFragment fragment = new MapHomeFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_HOME_LIST, homeList);
        args.putParcelable(ARG_LOCATION, zoomLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.e(TAG, "onCreate. Getting argument location");
            mZoomLocation = getArguments().getParcelable(ARG_LOCATION);
            mHomeList = getArguments().getParcelableArrayList(ARG_HOME_LIST);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(this);

        mAddressPanel = (LinearLayout) view.findViewById(R.id.map_address_panel);
        mAddressPanel.setVisibility(View.GONE);
        mAddressTextView = (TextView) view.findViewById(R.id.map_address_text_view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        //Zoom to location
        if (mGoogleMap != null) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            //Listen for Marker clicks
            mGoogleMap.setOnMarkerClickListener(this);
            mGoogleMap.setOnMapLoadedCallback(this);
            //Set window adapter so we can create a custom window with and image
            mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
            //Set up click events on Info Window(Picture of Home)
            mGoogleMap.setOnInfoWindowClickListener(this);

            setMapPadding();

            for (int i = 0; i < mHomeList.size(); i++) {

                addMarker(mHomeList.get(i));
            }

            //Add Fourth Ward Park Market Last so it will have an id of mHomeList.size()
            MarkerOptions options = new MarkerOptions()
                    .position(Util.getFourthWardParkLocation())
                    .title(getString(R.string.marker_buy_tickets));
            mGoogleMap.addMarker(options);

            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mZoomLocation, 15));

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        mAddressTextView.setText(marker.getTitle());

        return false;
    }

    @Override
    public void onMapLoaded() {

        Log.e(TAG, "onMapLoaded()");

        //Slide up address view
        Animation animShow = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up);
        mAddressPanel.setVisibility(View.VISIBLE);
        mAddressPanel.startAnimation(animShow);


    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e(TAG, "onInfoWindowClick() with marker id = " + marker.getId());

        int index = getHomePositionFromMarker(marker);

        //Only pull up Home Detail activity if this marker is a home in the list and not the ticket
        //location
        if(index < mHomeList.size()) {
            Intent intent = new Intent(getActivity(), FeaturedHomeDetailActivity.class);

            intent.putExtra(EXTRA_HOME_POSITION, index);
            intent.putParcelableArrayListExtra(EXTRA_HOME_LIST, mHomeList);
            startActivity(intent);
        }


    }
    /*********************************************************************************************/
    /*                                     Private Methods                                       */
    /*********************************************************************************************/
    private void setMapPadding() {

        int height = mAddressPanel.getMeasuredHeight();
        Log.e(TAG, "address panel height = " + height);
        mGoogleMap.setPadding(0, 0, 0, height - 16);
    }

    private void addMarker(Home home) {

        if (home.getLocation() != null) {
            MarkerOptions options = new MarkerOptions()
                    .position(home.getLocation())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(home.getName());

            mGoogleMap.addMarker(options);
        }
    }

    private int getHomePositionFromMarker(Marker marker) {

        try {
            String strId = marker.getId().substring(1, 2);
            int index = Integer.parseInt(strId);
            return index;

        } catch (StringIndexOutOfBoundsException e) {
            Log.e(TAG, "render() Caught StringIndexOutOfBounds Exception " + e.getMessage());
            return -1;
        }


    }


    /********************************************************************************************/
    /*                                   Inner Classes                                          */
    /********************************************************************************************/
    public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mWindow;
        // private int mHomePosition;
        private Marker mCurrentMarker;

        CustomInfoWindowAdapter() {

            //  mHomePosition = homePosition;

            mWindow = getLayoutInflater(null).inflate(R.layout.map_marker_image, null);

        }

        @Override
        public View getInfoWindow(final Marker marker) {
            Log.e(TAG, "getInfoWindow()");
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (mCurrentMarker != null &&
                    mCurrentMarker.isInfoWindowShown()) {
                mCurrentMarker.hideInfoWindow();
                mCurrentMarker.showInfoWindow();
            }
            return null;
        }

        private void render(final Marker marker, View view) {
            mCurrentMarker = marker;
            mCurrentMarker.hideInfoWindow();

            final ImageView markerImage = (ImageView) view.findViewById(R.id.map_marker_image_view);

            Log.e(TAG, "render() with marker id = " + marker.getId());


            //Get the home that this marker points to.
            int index = getHomePositionFromMarker(marker);
            Log.e(TAG, "render() with index = " + index);

            if (index >= 0 && index < mHomeList.size()) {
                Log.e(TAG, "render() Loading image at " + mHomeList.get(index).getImageUrl());
                Picasso.with(getContext()).load(mHomeList.get(index).getImageUrl()).into(markerImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "render() success loading image");
                        getInfoContents(marker);

                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "render() failed loading image");
                    }
                });
            } else if(index >= mHomeList.size()) {
                Picasso.with(getContext()).load(R.drawable.ticket).into(markerImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG,"render() success loading image for tickets marker");
                        getInfoContents(marker);
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "render() failed loading ticket image");
                    }
                });
            }


        }
    }
}
