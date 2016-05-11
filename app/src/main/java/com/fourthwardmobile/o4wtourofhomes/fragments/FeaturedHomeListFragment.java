package com.fourthwardmobile.o4wtourofhomes.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.adapters.HomeListAdapter;
import com.fourthwardmobile.o4wtourofhomes.models.Home;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeaturedHomeListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeaturedHomeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeaturedHomeListFragment extends Fragment {

    /************************************************************************************/
    /*                                    Constants                                     */
    /************************************************************************************/
    private static final String TAG = FeaturedHomeListFragment.class.getSimpleName();

    private static final String ARG_HOME_LIST = "home_list";

    /************************************************************************************/
    /*                                    Local Data                                    */
    /************************************************************************************/
    private ArrayList<Home> mHomeList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //private OnFragmentInteractionListener mListener;

    public FeaturedHomeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeaturedHomeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeaturedHomeListFragment newInstance(ArrayList<Home> homeList) {
        FeaturedHomeListFragment fragment = new FeaturedHomeListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_HOME_LIST, homeList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHomeList = getArguments().getParcelableArrayList(ARG_HOME_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_featured_home_list, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.home_list_recycler_view);
        //Create Grid with 2 columns
        mLayoutManager = new GridLayoutManager(getContext(),2);
        //Set Layout Manager for RecyclerView
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Initialize the Adapter
        mAdapter = new HomeListAdapter(getContext(),mHomeList);
        //Set the adapter for the RecyclerView;
        mRecyclerView.setAdapter(mAdapter);

        Log.e(TAG,"onCreateView() Got home list count = " + mHomeList.size());
        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
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
//
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
}
