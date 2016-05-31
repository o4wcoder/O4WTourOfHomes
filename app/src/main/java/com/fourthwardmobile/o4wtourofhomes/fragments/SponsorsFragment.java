package com.fourthwardmobile.o4wtourofhomes.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.adapters.SponsorListAdapter;
import com.fourthwardmobile.o4wtourofhomes.models.Sponsor;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SponsorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SponsorsFragment extends Fragment {

   /*****************************************************************************************/
   /*                                 Constants                                             */
   /*****************************************************************************************/
   private static final String TAG = SponsorsFragment.class.getSimpleName();

    private static final String ARG_SPONSOR_LIST = "sponsor_list";

    /****************************************************************************************/
    /*                                 Local Data                                           */
    /****************************************************************************************/
    private ArrayList<Sponsor> mSponsorList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    public SponsorsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SponsorsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SponsorsFragment newInstance(ArrayList<Sponsor> sponsorList) {
        SponsorsFragment fragment = new SponsorsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SPONSOR_LIST,sponsorList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mSponsorList = getArguments().getParcelableArrayList(ARG_SPONSOR_LIST);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sponsors, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.sponsor_list_recycler_view);
        //Set list type of layout for RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new SponsorListAdapter(getContext(),mSponsorList, new SponsorListAdapter.SponsorListAdapterOnClickHandler() {
            @Override
            public void onClick(int pos, SponsorListAdapter.SponsorListAdapterViewHolder vh) {

                //Bring up sponsor webpage
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSponsorList.get(pos).getWebsite()));

                if(intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

}
