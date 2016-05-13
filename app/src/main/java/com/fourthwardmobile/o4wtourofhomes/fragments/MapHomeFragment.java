package com.fourthwardmobile.o4wtourofhomes.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.helpers.Util;
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
import com.google.android.gms.maps.model.MarkerOptions;

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
public class MapHomeFragment extends Fragment implements OnMapReadyCallback {

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
        args.putParcelableArrayList(ARG_HOME_LIST,homeList);
        args.putParcelable(ARG_LOCATION, zoomLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.e(TAG,"onCreate. Getting argument location");
            mZoomLocation = getArguments().getParcelable(ARG_LOCATION);
            mHomeList = getArguments().getParcelableArrayList(ARG_HOME_LIST);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_map, container, false);

        mMapView = (MapView)view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(this);

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
        if(mGoogleMap != null) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


            for(int i = 0; i < mHomeList.size(); i++) {

               addMarker(mHomeList.get(i));
            }

            //Add Fourth Ward Park Market
            MarkerOptions options = new MarkerOptions()
                    .position(Util.getFourthWardParkLocation())
                    .title(getString(R.string.marker_buy_tickets));
            mGoogleMap.addMarker(options);

            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mZoomLocation,15));

        }
    }

    private void addMarker(Home home) {

        if(home.getLocation() != null) {
            MarkerOptions options = new MarkerOptions()
                    .position(home.getLocation())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(home.getName());

            mGoogleMap.addMarker(options);
        }


    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.e(TAG,"onDestroyView()");
//
//        /**
//         * Remove child Map Framgment when we leave the parent fragment. If not,
//         * it will crash when we return.
//         */
//        android.app.Fragment fragment = getActivity().getFragmentManager()
//                .findFragmentById(R.id.map);
//        if (null != fragment) {
//            Log.e(TAG,"onDestroyView() remove map fragment");
//            android.app.FragmentTransaction ft = getActivity()
//                    .getFragmentManager().beginTransaction();
//            ft.remove(fragment);
//            ft.commit();
//        }
//    }



}
