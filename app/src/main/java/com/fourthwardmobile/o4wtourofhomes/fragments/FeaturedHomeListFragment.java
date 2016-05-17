package com.fourthwardmobile.o4wtourofhomes.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.activities.FeaturedHomeDetailActivity;
import com.fourthwardmobile.o4wtourofhomes.adapters.FeaturedHomeListAdapter;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
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
        //mLayoutManager = new GridLayoutManager(getContext(),2);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //Set Layout Manager for RecyclerView
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Initialize the Adapter
        mAdapter = new FeaturedHomeListAdapter(getContext(),mHomeList, new FeaturedHomeListAdapter.HomeListAdapterOnClickHandler() {
            @Override
            public void onClick(int position, FeaturedHomeListAdapter.FeaturedHomeListAdapterViewHolder vh) {
                Log.e(TAG,"FeaturedHomeListFragment() onClick at position = " + position + " trans name = " + vh.thumbnailImageView.getTransitionName());


                Intent intent = new Intent(getActivity(),FeaturedHomeDetailActivity.class);
                intent.putExtra(EXTRA_HOME_POSITION,position);
                intent.putExtra(EXTRA_HOME_LIST,mHomeList);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    Log.e(TAG,"onClick() with transition name = " + vh.thumbnailImageView.getTransitionName());
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            new Pair<View,String>(vh.thumbnailImageView,vh.thumbnailImageView.getTransitionName()));
                    startActivity(intent,options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
        //Set the adapter for the RecyclerView;
        mRecyclerView.setAdapter(mAdapter);

        Log.e(TAG,"onCreateView() Got home list count = " + mHomeList.size());
        return view;
    }

}
