package com.fourthwardmobile.o4wtourofhomes.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.models.Home;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeaturedHomeDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeaturedHomeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeaturedHomeDetailFragment extends Fragment {

    /************************************************************************************/
    /*                                  Constants                                       */
    /************************************************************************************/
    private static final String TAG = FeaturedHomeDetailFragment.class.getSimpleName();
    private static final String ARG_IMAGE_POSITION = "position";
    private static final String ARG_HOME = "home";

    /************************************************************************************/
    /*                                  Local Data                                      */
    /************************************************************************************/
    private int mPosition;
    private Home mHome;

    private TextView mHomeNameTextView;

   // private OnFragmentInteractionListener mListener;

    public FeaturedHomeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeaturedHomeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeaturedHomeDetailFragment newInstance(int position) {
        FeaturedHomeDetailFragment fragment = new FeaturedHomeDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_POSITION, position);
      //  args.putParcelable(ARG_HOME, home);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_IMAGE_POSITION);

            //Get the Home at the slected position in the list
            mHome = ((OnFragmentCallback)getActivity()).getHome(mPosition);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_detail, container, false);

       // ((TextView)view.findViewById(R.id.detail_text_view)).setText("Got home = " + ((OnFragmentCallback)getActivity()).getHome(mPosition).getName());

        ImageView homeImageView = (ImageView)view.findViewById(R.id.detail_home_image);

        Picasso.with(getActivity()).load(mHome.getImageUrl()).into(homeImageView);

        mHomeNameTextView = (TextView)view.findViewById(R.id.detail_home_name);
        mHomeNameTextView.setText(mHome.getName());
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    public interface  OnFragmentCallback {

        Home getHome(int position);
    }
}
