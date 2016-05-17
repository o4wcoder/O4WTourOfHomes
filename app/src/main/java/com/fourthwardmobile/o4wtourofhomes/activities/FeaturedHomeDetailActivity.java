package com.fourthwardmobile.o4wtourofhomes.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.fragments.FeaturedHomeDetailFragment;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
import com.fourthwardmobile.o4wtourofhomes.models.Home;

import java.util.ArrayList;

public class FeaturedHomeDetailActivity extends AppCompatActivity implements Constants,
 FeaturedHomeDetailFragment.OnFragmentCallback{

    /**********************************************************************************/
    /*                                Constants                                       */
    /**********************************************************************************/

    /**********************************************************************************/
    /*                                Local Data                                      */
    /**********************************************************************************/
    int mStartingPosition;
    ArrayList<Home> mHomeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

        setContentView(R.layout.activity_home_detail);



        mStartingPosition = getIntent().getIntExtra(EXTRA_HOME_POSITION,0);
        mHomeList = getIntent().getParcelableArrayListExtra(EXTRA_HOME_LIST);

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new FeaturedHomeDetailFragmentPagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(mStartingPosition);
    }

    @Override
    public Home getHome(int position) {

        if(mHomeList != null)
            return mHomeList.get(position);
        else
            return null;
    }

    private class FeaturedHomeDetailFragmentPagerAdapter extends FragmentStatePagerAdapter  {

        public FeaturedHomeDetailFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FeaturedHomeDetailFragment.newInstance(position);
        }


        @Override
        public int getCount() {

            return mHomeList.size();
        }
    }




}
