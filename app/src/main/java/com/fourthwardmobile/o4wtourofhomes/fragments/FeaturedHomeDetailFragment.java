package com.fourthwardmobile.o4wtourofhomes.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.helpers.ImageTransitionListener;
import com.fourthwardmobile.o4wtourofhomes.helpers.Util;
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
    int TEXT_FADE_DURATION = 500;

    /************************************************************************************/
    /*                                  Local Data                                      */
    /************************************************************************************/
    private int mPosition;
    private Home mHome;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private int mMutedColor = 0xFF333333;

    private TextView mHomeNameTextView;
    ImageView mHomeImageView;

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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {

            if (getArguments() != null) {
                mPosition = getArguments().getInt(ARG_IMAGE_POSITION);
                //Get the Home at the slected position in the list
                mHome = ((OnFragmentCallback) getActivity()).getHome(mPosition);
            }
        }
        else {
            mPosition = savedInstanceState.getInt(ARG_IMAGE_POSITION);
            mHome = savedInstanceState.getParcelable(ARG_HOME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_detail, container, false);

         mHomeImageView = (ImageView)view.findViewById(R.id.detail_home_image);
        //Check for profile title enter shared transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHomeImageView.setTransitionName(Util.getTransitionName(getActivity(), mPosition));
            getActivity().getWindow().getSharedElementEnterTransition().addListener(new ImageTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {

                    //End of transition, fade in days of week row
                    mHomeNameTextView.animate().setDuration(TEXT_FADE_DURATION).alpha(1f);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    //Start of transition, make days of week row invisible
                    mHomeNameTextView.setAlpha(0f);
                }
            });
        }

        //Get CollapsingToolbarLayout
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);

        Picasso.with(getActivity()).load(mHome.getImageUrl()).into(mHomeImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG,"Picasso Callback. Finished loading image. ");
                        Bitmap bitmap = ((BitmapDrawable)mHomeImageView.getDrawable()).getBitmap();
                        Palette p = Palette.generate(bitmap, 12);
                        mMutedColor = p.getDarkMutedColor(0xFF333333);

                        //Set title and colors for collapsing toolbar
                        mCollapsingToolbarLayout.setTitle(mHome.getName());
                        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

                        //Set content descriptioni for toolbar/title
                        mCollapsingToolbarLayout.setContentDescription(mHome.getName());

                        //Set pallet colors when toolbar is collapsed
                        int primaryColor = getResources().getColor(R.color.colorPrimary);
                        int primaryDarkColor = getResources().getColor(R.color.colorPrimaryDark);
                        mCollapsingToolbarLayout.setContentScrimColor(p.getMutedColor(primaryColor));
                        mCollapsingToolbarLayout.setStatusBarScrimColor(p.getDarkMutedColor(primaryDarkColor));

                        //Now that we've successfully loaded the image, we can start the
                        //shared transition.
                        getActivity().supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {

                    }
                });

        mHomeNameTextView = (TextView)view.findViewById(R.id.detail_home_name);
        mHomeNameTextView.setText(mHome.getName());


        TextView ownerTextView = (TextView)view.findViewById(R.id.detail_owner_text_view);
        ownerTextView.setText(getSpannedString(getString(R.string.detail_header_owner),mHome.getOwners()));

        TextView homeTypeTextView = (TextView)view.findViewById(R.id.detail_home_type_text_view);
        homeTypeTextView.setText(getSpannedString(getString(R.string.detail_header_home_type),mHome.getHomeType()));

        TextView yearBuiltTextView = (TextView)view.findViewById(R.id.detail_year_built_text_view);
        yearBuiltTextView.setText(getSpannedString(getString(R.string.detail_header_year_built),mHome.getYearBuilt()));

        TextView sectionTextView = (TextView)view.findViewById(R.id.detail_section_text_view);
        sectionTextView.setText(getSpannedString(getString(R.string.detail_header_section),mHome.getSection()));


        view.findViewById(R.id.map_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    private Spanned getSpannedString(String heading, String body) {

        return Html.fromHtml("<b>" + heading + "</b>" + " " + body);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG,"onSaveInstanceState()");
        outState.putInt(ARG_IMAGE_POSITION,mPosition);
        outState.putParcelable(ARG_HOME,mHome);
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

    public interface OnImageLoadingCallback {

        void onImageLoaded();
    }
}
