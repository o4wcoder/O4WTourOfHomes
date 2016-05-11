package com.fourthwardmobile.o4wtourofhomes.activities;

import android.content.res.AssetManager;
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
import com.fourthwardmobile.o4wtourofhomes.interfaces.Constants;
import com.fourthwardmobile.o4wtourofhomes.models.Home;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constants{

    /******************************************************************************************/
    /*                                     Constants                                          */
    /******************************************************************************************/
    private static String TAG = MainActivity.class.getSimpleName();

    //XML Data Tags
    private static final String XML_TAG_HOME = "home";
    private static final String XML_TAG_ADDRESS = "address";
    private static final String XML_TAG_OWNER = "owner";
    private static final String XML_TAG_HOME_TYPE = "hometype";
    private static final String XML_TAG_YEAR = "year";
    private static final String XML_TAG_SECTION = "section";
    private static final String XML_TAG_LATITUDE = "latitude";
    private static final String XML_TAG_LONGITUDE = "longitude";
    private static final String XML_TAG_IMAGE = "image";

    /******************************************************************************************/
    /*                                     Local Data                                         */
    /******************************************************************************************/
    private GoogleApiClient mGoogleApiClient;
    private LatLng mFourthWardParkLocation;

    private ArrayList<Home> mHomeList = null;



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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.e(TAG,"onCreate() Load Home Data");
        loadHomeData();

        //Start with Home Fragment
        HomeFragment firstFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,firstFragment).commit();




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
            fragment = new MapHomeFragment();

        } else if (id == R.id.nav_tickets) {

        } else if (id == R.id.nav_contact) {

        }

        if(fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragment_container,fragment);
            transaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadHomeData() {

        //String strXml = null;
        Log.e(TAG,"loadHomeData() Inside");

        XmlPullParserFactory pullParserFactory;
        try {

            //Get Pull Parser instance
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream inputStream = getAssets().open(FILE_HOMES_DATA);

//            int length = inputStream.available();
//            Log.e(TAG, "Got length of file = " + length);
//            byte[] data = new byte[length];
//            inputStream.read(data);
//            inputStream.close();
//            String strXml = new String(data);

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            //Set Input stream to the XML file as the source for the parser
            parser.setInput(inputStream,null);

            parseXML(parser);

        } catch (XmlPullParserException e) {

        } catch (IOException e) {
            Log.e(TAG,"Exception loading XML data file. " + e.getMessage());
        }
    }

    private void parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {



        int eventType = parser.getEventType();

        Home currentHome = null;

        //Got through each line of XML till the end
        while(eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;

            switch(eventType) {
                case XmlPullParser.START_DOCUMENT:
                    mHomeList = new ArrayList<>();

                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();

                    if(name.equalsIgnoreCase(XML_TAG_HOME)) {

                        currentHome = new Home();
                    }
                    else if (currentHome != null) {

                        if(name.equalsIgnoreCase(XML_TAG_ADDRESS)) {
                            currentHome.setAddress(parser.nextText());
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
                        else if(name.equalsIgnoreCase(XML_TAG_LATITUDE)) {
                            currentHome.setLatitude(parser.nextText());
                        }
                        else if(name.equalsIgnoreCase(XML_TAG_LONGITUDE)) {
                            currentHome.setLongitude(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if(name.equalsIgnoreCase(XML_TAG_HOME) && currentHome != null) {
                        mHomeList.add(currentHome);
                    }
            }
            eventType = parser.next();
        }

        Log.e(TAG,"End parsing XML, got number of homes = " + mHomeList.size());
    }


}
