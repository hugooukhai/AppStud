package com.example.adminpc.appstud;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {


   private ViewPager mPager;
   private ViewPagerAdapter mPagerAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    Log.d("selected ","map");
                 // Display the fragment
                mPager.setCurrentItem(0);


                    return true;
                case R.id.navigation_list:
                    Log.d("selected ","list");
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
        mPagerAdapter.addFragment(new MapFragment());
        mPagerAdapter.addFragment(new ListFragment());
        mPager.setAdapter(mPagerAdapter);


    }


}
