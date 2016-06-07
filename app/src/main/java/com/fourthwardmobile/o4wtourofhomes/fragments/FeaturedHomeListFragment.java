package com.fourthwardmobile.o4wtourofhomes.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.activities.FeaturedHomeDetailActivity;
import com.fourthwardmobile.o4wtourofhomes.activities.MainActivity;
import com.fourthwardmobile.o4wtourofhomes.adapters.FeaturedHomeListAdapter;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
import com.fourthwardmobile.o4wtourofhomes.models.Home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeaturedHomeListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeaturedHomeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeaturedHomeListFragment extends Fragment implements Constants{

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
        Log.e(TAG,"onCreate()");
        if (getArguments() != null) {
            mHomeList = getArguments().getParcelableArrayList(ARG_HOME_LIST);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_featured_home_list, container, false);
        Log.e(TAG,"onCreateView()");
        mRecyclerView = (RecyclerView)view.findViewById(R.id.home_list_recycler_view);
        //Create Grid with 2 columns
       // mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        //Set Layout Manager for RecyclerView
         //mRecyclerView.setLayoutManager(mLayoutManager);
         mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //Initialize the Adapter
        mAdapter = new FeaturedHomeListAdapter(getContext(),mHomeList, new FeaturedHomeListAdapter.HomeListAdapterOnClickHandler() {
            @Override
            public void onClick(int position, FeaturedHomeListAdapter.FeaturedHomeListAdapterViewHolder vh) {

                Intent intent = new Intent(getActivity(),FeaturedHomeDetailActivity.class);
                intent.putExtra(EXTRA_HOME_POSITION,position);
                intent.putExtra(EXTRA_HOME_LIST,mHomeList);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            new Pair<View,String>(vh.thumbnailImageView,vh.thumbnailImageView.getTransitionName()));
                    ActivityCompat.startActivity(getActivity(),intent,options.toBundle());

                } else {
                    startActivity(intent);
                }
            }
        });

        //Set the adapter for the RecyclerView;
      //  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.setAdapter(mAdapter);
       // }

        Log.e(TAG,"onCreateView() Got home list count = " + mHomeList.size());
        return view;
    }
//
//    @Override
//    public void onEnterAnimationComplete() {
//        super.onEnterAnimationComplete();
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.scheduleLayoutAnimation();
//    }


    public RecyclerView getRecyclerView() {

        return mRecyclerView;
    }





}
