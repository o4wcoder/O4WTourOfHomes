package com.fourthwardmobile.o4wtourofhomes.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourthwardmobile.o4wtourofhomes.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SponsorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SponsorsFragment extends Fragment {



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
    public static SponsorsFragment newInstance() {
        SponsorsFragment fragment = new SponsorsFragment();
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
        View view = inflater.inflate(R.layout.fragment_sponsors, container, false);

        return view;
    }

}
