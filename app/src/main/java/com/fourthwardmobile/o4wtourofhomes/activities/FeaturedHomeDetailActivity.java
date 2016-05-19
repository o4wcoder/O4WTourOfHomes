package com.fourthwardmobile.o4wtourofhomes.activities;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.fragments.FeaturedHomeDetailFragment;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
import com.fourthwardmobile.o4wtourofhomes.models.Home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FeaturedHomeDetailActivity extends AppCompatActivity implements Constants,
 FeaturedHomeDetailFragment.OnFragmentCallback{

    /**********************************************************************************/
    /*                                Constants                                       */
    /**********************************************************************************/
    private static String TAG = FeaturedHomeDetailActivity.class.getSimpleName();

    /**********************************************************************************/
    /*                                Local Data                                      */
    /**********************************************************************************/
    int mStartingPosition;
    int mCurrentPosition;

    ArrayList<Home> mHomeList;
    FeaturedHomeDetailFragment mCurrentDetailsFragment;
    private boolean mIsReturning;


    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                ImageView sharedElement = mCurrentDetailsFragment.getSharedImage();
                if (sharedElement == null) {
                    // If shared element is null, then it has been scrolled off screen and
                    // no longer visible. In this case we cancel the shared element transition by
                    // removing the shared element from the shared elements map.
                    names.clear();
                    sharedElements.clear();
                } else if (mStartingPosition != mCurrentPosition) {
                    // If the user has swiped to a different ViewPager page, then we need to
                    // remove the old shared element and replace it with the new shared element
                    // that should be transitioned instead.
                    names.clear();
                    names.add(sharedElement.getTransitionName());
                    sharedElements.clear();
                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

        setContentView(R.layout.activity_home_detail);

        supportPostponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterSharedElementCallback(mCallback);
        }


        mStartingPosition = getIntent().getIntExtra(EXTRA_HOME_POSITION,0);
        mHomeList = getIntent().getParcelableArrayListExtra(EXTRA_HOME_LIST);

        //Current pager position is the starting one when activity created
        if(savedInstanceState == null) {
            mCurrentPosition = mStartingPosition;
        }
        else {
            mCurrentPosition = savedInstanceState.getInt(EXTRA_CURRENT_HOME_POSITION);
        }

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new FeaturedHomeDetailFragmentPagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(mStartingPosition);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CURRENT_HOME_POSITION, mCurrentPosition);
    }

    @Override
    public void finishAfterTransition() {
        // Need to send back to calling activity, the current page in the pager
        mIsReturning = true;
        Intent data = new Intent();
        data.putExtra(EXTRA_HOME_POSITION, mStartingPosition);
        data.putExtra(EXTRA_CURRENT_HOME_POSITION, mCurrentPosition);

        //onActivityReenter in calling activity will receive the data.
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }
    @Override
    public Home getHome(int position) {

        if(mHomeList != null)
            return mHomeList.get(position);
        else
            return null;
    }

    /***************************************************************************************/
    /*                                 Inner Classes                                       */
    /***************************************************************************************/
    private class FeaturedHomeDetailFragmentPagerAdapter extends FragmentStatePagerAdapter  {

        public FeaturedHomeDetailFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FeaturedHomeDetailFragment.newInstance(mStartingPosition,position);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurrentDetailsFragment = (FeaturedHomeDetailFragment) object;
        }

        @Override
        public int getCount() {

            return mHomeList.size();
        }
    }




}
