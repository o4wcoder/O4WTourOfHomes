package com.fourthwardmobile.o4wtourofhomes.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.adapters.FeaturedHomeListAdapter;
import com.fourthwardmobile.o4wtourofhomes.fragments.ContactsFragment;
import com.fourthwardmobile.o4wtourofhomes.fragments.FeaturedHomeListFragment;
import com.fourthwardmobile.o4wtourofhomes.fragments.HomeFragment;
import com.fourthwardmobile.o4wtourofhomes.fragments.MapHomeFragment;
import com.fourthwardmobile.o4wtourofhomes.fragments.SponsorsFragment;
import com.fourthwardmobile.o4wtourofhomes.fragments.TicketsFragment;
import com.fourthwardmobile.o4wtourofhomes.helpers.Util;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
import com.fourthwardmobile.o4wtourofhomes.models.Home;
import com.fourthwardmobile.o4wtourofhomes.models.Sponsor;
import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constants, HomeFragment.OnFragmentCallback {

    /******************************************************************************************/
    /*                                     Constants                                          */
    /******************************************************************************************/
    private static String TAG = MainActivity.class.getSimpleName();

    //XML Home Data Tags
    private static final String XML_TAG_HOME = "home";
    private static final String XML_TAG_NAME = "name";
    private static final String XML_TAG_ADDRESS = "address";
    private static final String XML_TAG_OWNER = "owner";
    private static final String XML_TAG_HOME_TYPE = "hometype";
    private static final String XML_TAG_YEAR = "year";
    private static final String XML_TAG_SECTION = "section";
    private static final String XML_TAG_IMAGE = "image";

    //XML Sponsor Data Tags
    private static final String XML_TAG_SPONSOR = "sponsor";
    private static final String XML_TAG_DESCRIPTION = "description";
    private static final String XML_TAG_BACKGROUND = "background";
    private static final String XML_TAG_LOGO = "logo";
    private static final String XML_TAG_WEBSITE = "website";

    private static final String ARG_FIRST_TIME = "first_times";
    private static final String ARG_HOME_LIST = "home_list";
    /******************************************************************************************/
    /*                                     Local Data                                         */
    /******************************************************************************************/
    NavigationView mNavigationView;
    private ArrayList<Home> mHomeList = null;
    private ArrayList<Sponsor> mSponsorList = null;
    boolean mIsFirstTime = true;
    //Used for passedback data after a return transition
    private Bundle mTmpReenterState;

    private FeaturedHomeListFragment mHomeListFragment;


    private ImageView mTicketImageView;

    /**
     * Need a shared element callback for return transition. We need to see if the pager
     * has been swiped since the enter transition and the image has changed on return
     */
    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {

            if(mTmpReenterState != null) {
                int startingPosition = mTmpReenterState.getInt(EXTRA_HOME_POSITION);
                int currentPosition = mTmpReenterState.getInt(EXTRA_CURRENT_HOME_POSITION);
                
                if(startingPosition != currentPosition) {
                    //User has swipped the pager to a different fragment. Need to update the
                    //shared element transition name for return trip
                    String newTransitionName = Util.getTransitionName(getApplicationContext(),currentPosition);
                    if(mHomeListFragment != null) {

                        View newSharedElement = mHomeListFragment.getRecyclerView().findViewWithTag(newTransitionName);
                        if(newSharedElement != null) {

                            names.clear();
                            names.add(newTransitionName);
                            sharedElements.clear();
                            sharedElements.put(newTransitionName,newSharedElement);
                        }
                    }
                }

                mTmpReenterState = null;
            } else {
                //Activity is exiting
                View navigationBar = findViewById(android.R.id.navigationBarBackground);
                View statusBar = findViewById(android.R.id.statusBarBackground);
                if (navigationBar != null) {
                    names.add(navigationBar.getTransitionName());
                    sharedElements.put(navigationBar.getTransitionName(), navigationBar);
                }
                if (statusBar != null) {
                    names.add(statusBar.getTransitionName());
                    sharedElements.put(statusBar.getTransitionName(), statusBar);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setExitSharedElementCallback(mCallback);
        Log.e(TAG, "onCreate()");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        Log.e(TAG, "onCreate() Load Home Data");


        //Start with Home Fragment
        if (savedInstanceState == null) {
            //Load Home data and locations
            new LoadHomeDataTask().execute();
            new LoadSponsorDataTask().execute();

            Log.e(TAG, "Nothing saved, first time through. Set home fragment");
            HomeFragment firstFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();

            //Select Home menu on Navigation Drawer
            mNavigationView.setCheckedItem(R.id.nav_home);

            mIsFirstTime = false;
        } else {
            mIsFirstTime = savedInstanceState.getBoolean(ARG_FIRST_TIME);
            mHomeList = savedInstanceState.getParcelableArrayList(ARG_HOME_LIST);

            //If we got here, we may have rotated before the data was finished loading.
            //Go try and fetch it again
            if (mHomeList == null)
                new LoadHomeDataTask().execute();
            if(mSponsorList == null)
                new LoadSponsorDataTask().execute();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(ARG_FIRST_TIME, mIsFirstTime);
        outState.putParcelableArrayList(ARG_HOME_LIST, mHomeList);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = HomeFragment.newInstance();
        } else if (id == R.id.nav_featured_homes) {
            fragment = FeaturedHomeListFragment.newInstance(mHomeList);
            mHomeListFragment = (FeaturedHomeListFragment)fragment;

        } else if (id == R.id.nav_map) {
            fragment = MapHomeFragment.newInstance(mHomeList, Util.getFourthWardParkLocation());

        } else if (id == R.id.nav_tickets) {
            fragment = TicketsFragment.newInstance();

        } else if (id == R.id.nav_sponsors) {
            fragment = SponsorsFragment.newInstance(mSponsorList);

        } else if (id == R.id.nav_contact) {
            fragment = ContactsFragment.newInstance();
        }

        updateFragment(fragment);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityReenter(int requestCode, Intent data) {
        super.onActivityReenter(requestCode, data);
        Log.e(TAG, "onActivityReenter() ");

        if(requestCode == RESULT_OK) {
            Log.e(TAG, "onActivityReenter() got result ok");
            if(data != null) {
                Log.e(TAG, "onActivityReenter. Got some data!!");
                mTmpReenterState = new Bundle(data.getExtras());
                int startingPosition = mTmpReenterState.getInt(EXTRA_HOME_POSITION);
                int currentPosition = mTmpReenterState.getInt(EXTRA_CURRENT_HOME_POSITION);
                Log.e(TAG, "onActivityReenter() Got starting pos = " + startingPosition +
                        " current pos = " + currentPosition);
            }
        }

    }
    /***************************************************************************************/
    /*                                  Private Methods                                    */
    /***************************************************************************************/
    private void updateFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }

    }

    private XmlPullParser getXmlParser(String xmlFile) throws XmlPullParserException,IOException {
        XmlPullParserFactory pullParserFactory;
        //Get Pull Parser instance
        pullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = pullParserFactory.newPullParser();

        InputStream inputStream = getAssets().open(xmlFile);

        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        //Set Input stream to the XML file as the source for the parser
        parser.setInput(inputStream, null);

        return parser;
    }

    private ArrayList<Home> loadHomeData() {

        //String strXml = null;
        Log.e(TAG, "loadHomeData() Inside");

        try {

            return parseHomeXML(getXmlParser(FILE_HOMES_DATA));

        } catch (XmlPullParserException e) {
            return null;

        } catch (IOException e) {
            Log.e(TAG, "Exception loading XML data file. " + e.getMessage());
            return null;
        }
    }

    private ArrayList<Sponsor> loadSponsorData() {

        //String strXml = null;
        Log.e(TAG, "loadSponsorData() Inside");

        try {

            return parseSponsorXML(getXmlParser(FILE_SPONSORS_DATA));

        } catch (XmlPullParserException e) {
            return null;

        } catch (IOException e) {
            Log.e(TAG, "Exception loading XML data file. " + e.getMessage());
            return null;
        }
    }

    private ArrayList<Sponsor> parseSponsorXML(XmlPullParser parser) throws XmlPullParserException, IOException {

        int eventType = parser.getEventType();

        Sponsor currentSponsor = null;
        ArrayList<Sponsor> sponsorList = null;

        //Go through each line of XML till the end
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;

            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    sponsorList = new ArrayList<>();

                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();

                    if(name.equalsIgnoreCase(XML_TAG_SPONSOR)) {
                        currentSponsor = new Sponsor();
                    } else if(currentSponsor != null) {

                        if(name.equalsIgnoreCase(XML_TAG_NAME)) {
                            currentSponsor.setName(parser.nextText());
                        } else if(name.equalsIgnoreCase(XML_TAG_DESCRIPTION)) {
                            currentSponsor.setDescription(parser.nextText());
                        } else if(name.equalsIgnoreCase(XML_TAG_BACKGROUND)) {
                            currentSponsor.setBackgroundImageUrl(parser.nextText());
                        } else if(name.equalsIgnoreCase(XML_TAG_LOGO)) {
                            currentSponsor.setLogoImageUrl(parser.nextText());
                        } else if(name.equalsIgnoreCase(XML_TAG_WEBSITE)) {
                            currentSponsor.setWebsite(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase(XML_TAG_SPONSOR) && currentSponsor != null) {
                        sponsorList.add(currentSponsor);
                    }
            }
            eventType = parser.next();
        }

        Log.e(TAG, "End parsing XML, got number of sponsors = " + sponsorList.size());

        return sponsorList;
    }
    private ArrayList<Home> parseHomeXML(XmlPullParser parser) throws XmlPullParserException, IOException {


        int eventType = parser.getEventType();

        Home currentHome = null;
        ArrayList<Home> homeList = null;

        //Go through each line of XML till the end
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;

            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    homeList = new ArrayList<>();

                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();

                    if (name.equalsIgnoreCase(XML_TAG_HOME)) {

                        currentHome = new Home();
                    } else if (currentHome != null) {

                        if (name.equalsIgnoreCase(XML_TAG_NAME)) {
                            currentHome.setName(parser.nextText());
                        }
                        if (name.equalsIgnoreCase(XML_TAG_ADDRESS)) {
                            currentHome.setAddress(parser.nextText());

                            try {

                                if (Util.isNetworkAvailable(this)) {
                                    List<Address> addresses;
                                    Geocoder mGeocoder = new Geocoder(getApplicationContext());
                                    addresses = mGeocoder.getFromLocationName(currentHome.getAddress(), 1);

                                    if (addresses.size() > 0) {
                                        currentHome.setLocation(new LatLng(addresses.get(0).getLatitude(),
                                                addresses.get(0).getLongitude()));
                                    } else
                                        Log.e(TAG, "Did not get any return from geocoder for street address = " + currentHome.getAddress());
                                } else {
                                    Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                Log.e(TAG, "Got exception with goecoder " + e.getMessage());
                            }
                        } else if (name.equalsIgnoreCase(XML_TAG_OWNER)) {
                            currentHome.setOwners(parser.nextText());
                        } else if (name.equalsIgnoreCase(XML_TAG_HOME_TYPE)) {
                            currentHome.setHomeType(parser.nextText());
                        } else if (name.equalsIgnoreCase(XML_TAG_YEAR)) {
                            currentHome.setYearBuilt(parser.nextText());
                        } else if (name.equalsIgnoreCase(XML_TAG_SECTION)) {
                            currentHome.setSection(parser.nextText());
                        } else if (name.equalsIgnoreCase(XML_TAG_IMAGE)) {
                            currentHome.setImageUrl(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase(XML_TAG_HOME) && currentHome != null) {
                        homeList.add(currentHome);
                    }
            }
            eventType = parser.next();
        }

        Log.e(TAG, "End parsing XML, got number of homes = " + homeList.size());

        return homeList;
    }

    @Override
    public void onSetMapFragment() {
        updateFragment(MapHomeFragment.newInstance(mHomeList, Util.getFourthWardParkLocation()));

        //Update Navigation Drawer to select the Map Menu
        mNavigationView.setCheckedItem(R.id.nav_map);
    }


    private class LoadHomeDataTask extends AsyncTask<Void, Void, ArrayList<Home>> {

        protected ArrayList<Home> doInBackground(Void... params) {

            return loadHomeData();


        }

        protected void onPostExecute(ArrayList<Home> homeList) {

            mHomeList = homeList;

            Log.e(TAG, "Finished AsynTask with home list size = " + mHomeList.size());
        }
    }

    private class LoadSponsorDataTask extends AsyncTask<Void, Void, ArrayList<Sponsor>> {

        protected ArrayList<Sponsor> doInBackground(Void... params) {

            return loadSponsorData();


        }

        protected void onPostExecute(ArrayList<Sponsor> sponsorList) {

            mSponsorList = sponsorList;

            Log.e(TAG, "Finished AsynTask with sponsor list size = " + mSponsorList.size());
        }
    }
}
