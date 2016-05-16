package com.fourthwardmobile.o4wtourofhomes.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.fragments.FeaturedHomeListFragment;
import com.fourthwardmobile.o4wtourofhomes.fragments.HomeFragment;
import com.fourthwardmobile.o4wtourofhomes.fragments.MapHomeFragment;
import com.fourthwardmobile.o4wtourofhomes.helpers.Util;
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
import com.fourthwardmobile.o4wtourofhomes.models.Home;
import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constants, HomeFragment.OnFragmentCallback
       {

    /******************************************************************************************/
    /*                                     Constants                                          */
    /******************************************************************************************/
    private static String TAG = MainActivity.class.getSimpleName();

    //XML Data Tags
    private static final String XML_TAG_HOME = "home";
    private static final String XML_TAG_NAME = "name";
    private static final String XML_TAG_ADDRESS = "address";
    private static final String XML_TAG_OWNER = "owner";
    private static final String XML_TAG_HOME_TYPE = "hometype";
    private static final String XML_TAG_YEAR = "year";
    private static final String XML_TAG_SECTION = "section";
    private static final String XML_TAG_IMAGE = "image";

    private static final String ARG_FIRST_TIME = "first_times";
    private static final String ARG_HOME_LIST = "home_list";
    /******************************************************************************************/
    /*                                     Local Data                                         */
    /******************************************************************************************/
    NavigationView mNavigationView;
    private ArrayList<Home> mHomeList = null;
    boolean mIsFirstTime = true;



    private ImageView mTicketImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG,"onCreate()");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        Log.e(TAG,"onCreate() Load Home Data");


        //Start with Home Fragment
        if(savedInstanceState == null) {
            //Load Home data and locations
            new LoadDataTask().execute();

            Log.e(TAG,"Nothing saved, first time through. Set home fragment");
            HomeFragment firstFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();

            //Select Home menu on Navigation Drawer
            mNavigationView.setCheckedItem(R.id.nav_home);

            mIsFirstTime = false;
        }
        else {
            mIsFirstTime = savedInstanceState.getBoolean(ARG_FIRST_TIME);
            mHomeList = savedInstanceState.getParcelableArrayList(ARG_HOME_LIST);

            //If we got here, we may have rotated before the data was finished loading.
            //Go try and fetch it again
            if(mHomeList == null)
                new LoadDataTask().execute();
        }



    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putBoolean(ARG_FIRST_TIME,mIsFirstTime);
        outState.putParcelableArrayList(ARG_HOME_LIST,mHomeList);


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

        if(id == R.id.nav_home) {
            fragment = HomeFragment.newInstance();
        }
        else if (id == R.id.nav_featured_homes) {
            fragment = FeaturedHomeListFragment.newInstance(mHomeList);

        } else if (id == R.id.nav_map) {
            fragment = MapHomeFragment.newInstance(mHomeList, Util.getFourthWardParkLocation());

        } else if (id == R.id.nav_tickets) {

        } else if (id == R.id.nav_contact) {

        }

        updateFragment(fragment);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateFragment(Fragment fragment) {
        if(fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragment_container,fragment);
            transaction.commit();
        }

    }
    private ArrayList<Home> loadHomeData() {

        //String strXml = null;
        Log.e(TAG,"loadHomeData() Inside");

        XmlPullParserFactory pullParserFactory;
        try {

            //Get Pull Parser instance
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream inputStream = getAssets().open(FILE_HOMES_DATA);

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            //Set Input stream to the XML file as the source for the parser
            parser.setInput(inputStream,null);

            return parseXML(parser);

        } catch (XmlPullParserException e) {
            return null;

        } catch (IOException e) {
            Log.e(TAG,"Exception loading XML data file. " + e.getMessage());
            return null;
        }
    }

    private ArrayList<Home> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {


        int eventType = parser.getEventType();

        Home currentHome = null;
        ArrayList<Home> homeList = null;

        //Got through each line of XML till the end
        while(eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;

            switch(eventType) {
                case XmlPullParser.START_DOCUMENT:
                    homeList = new ArrayList<>();

                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();

                    if(name.equalsIgnoreCase(XML_TAG_HOME)) {

                        currentHome = new Home();
                    }
                    else if (currentHome != null) {

                        if(name.equalsIgnoreCase(XML_TAG_NAME)) {
                            currentHome.setName(parser.nextText());
                        }
                        if(name.equalsIgnoreCase(XML_TAG_ADDRESS)) {
                            currentHome.setAddress(parser.nextText());

                            try {

                                if(Util.isNetworkAvailable(this)) {
                                    List<Address> addresses;
                                    Geocoder mGeocoder = new Geocoder(getApplicationContext());
                                    addresses = mGeocoder.getFromLocationName(currentHome.getAddress(), 1);

                                    if (addresses.size() > 0) {
                                        currentHome.setLocation(new LatLng(addresses.get(0).getLatitude(),
                                                addresses.get(0).getLongitude()));
                                    } else
                                        Log.e(TAG, "Did not get any return from geocoder for street address = " + currentHome.getAddress());
                                }
                                else {
                                    Toast.makeText(this,getString(R.string.no_network),Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (IOException e) {
                                Log.e(TAG,"Got exception with goecoder " + e.getMessage());
                            }
                        }
                        else if(name.equalsIgnoreCase(XML_TAG_OWNER)) {
                            currentHome.setOwners(parser.nextText());
                        }
                        else if(name.equalsIgnoreCase(XML_TAG_HOME_TYPE)) {
                            currentHome.setHomeType(parser.nextText());
                        }
                        else if(name.equalsIgnoreCase(XML_TAG_YEAR)) {
                            currentHome.setYearBuilt(parser.nextText());
                        }
                        else if(name.equalsIgnoreCase(XML_TAG_SECTION)) {
                            currentHome.setSection(parser.nextText());
                        }
                        else if(name.equalsIgnoreCase(XML_TAG_IMAGE)) {
                            currentHome.setImageUrl(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if(name.equalsIgnoreCase(XML_TAG_HOME) && currentHome != null) {
                        homeList.add(currentHome);
                    }
            }
            eventType = parser.next();
        }

        Log.e(TAG,"End parsing XML, got number of homes = " + homeList.size());

        return homeList;
    }

    @Override
    public void onSetMapFragment() {
        updateFragment(MapHomeFragment.newInstance(mHomeList, Util.getFourthWardParkLocation()));

        //Update Navigation Drawer to select the Map Menu
        mNavigationView.setCheckedItem(R.id.nav_map);
    }



    private class LoadDataTask extends AsyncTask<Void, Void, ArrayList<Home>> {

        protected ArrayList<Home> doInBackground(Void... params) {

            return loadHomeData();

        }

        protected void onPostExecute(ArrayList<Home> homeList) {

            mHomeList = homeList;

            Log.e(TAG,"Finished AsynTask with home list size = " + mHomeList.size());
        }
    }
}
