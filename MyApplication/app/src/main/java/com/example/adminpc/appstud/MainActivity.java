package com.example.adminpc.appstud;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.adminpc.appstud.Adapter.ViewPagerAdapter;
import com.example.adminpc.appstud.Fragment.ListFragment;
import com.example.adminpc.appstud.Fragment.MapFragment;
import com.example.adminpc.appstud.Model.Places;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks {


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private ViewPager mPager;
    private ViewPagerAdapter mPagerAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private MapFragment mMapFragment;
    private ListFragment mListFragment;
    private LatLng myPosition;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    Log.d("selected ", "map");
                    // Display the fragment
                    mPager.setCurrentItem(0);


                    return true;
                case R.id.navigation_list:
                    Log.d("selected ", "list");
                    // Display the fragment
                    mPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Add fragment to the pager, order matters for menu binding
        mMapFragment = new MapFragment();
        mListFragment = new ListFragment();
        mPagerAdapter.addFragment(mMapFragment);
        mPagerAdapter.addFragment(mListFragment);
        mPager.setAdapter(mPagerAdapter);



        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission ", "User didn't accepted geoloc yet, ask for it");

            // Ask for permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {
            //we got permission, get last location
          getLocation();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.unregisterConnectionCallbacks(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted, get location
                    getLocation();


                } else {
                    // permission not granted
                    Toast.makeText(this,"Location permission not granted",Toast.LENGTH_SHORT);
                }
                return;
            }
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                //we got last location, show it on map
                Log.d("latitude ", "" + mLastLocation.getLatitude());
                Log.d("longitude ", "" + mLastLocation.getLongitude());
                myPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMapFragment.updateMap(myPosition);
                lookForBars();



            } else {
                //we don't have location, ask for update LocationRequest locationRequest = new LocationRequest();
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setNumUpdates(1);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                Log.d("Launch", "Location request");
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        mLastLocation = location;
                        Log.d("latitude ", " on loc changed " + mLastLocation.getLatitude());
                        Log.d("longitude ", " on loc changed " + mLastLocation.getLongitude());
                        myPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        mMapFragment.updateMap(myPosition);
                        lookForBars();
                    }
                });
            }
        }
    }

    public void lookForBars(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Use OKHtttp and GSON libraries to easily get and parse Json
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+mLastLocation.getLatitude()+","+mLastLocation.getLongitude()+"&radius=2000&type=bar&key=AIzaSyBNJyNzvROoFZfTmo529RLpGO110sxUoGo").build();
                Gson gson = new Gson();

                try {
                    Response response = client.newCall(request).execute();

                    final Places placesResponse = gson.fromJson(response.body().string(),Places.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMapFragment.addMarkers(placesResponse);
                            ((ListFragment) mPagerAdapter.getItem(1)).setupRecyclerView(placesResponse);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
