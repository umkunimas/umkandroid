package com.example.rog.umk;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.rog.umk.Homepage.Homepage;
import com.example.rog.umk.Login_Reg.login;
import com.example.rog.umk.Order.orderHistory;
import com.example.rog.umk.Product.ProductSearch;
import com.example.rog.umk.Profile.adminProfile;
import com.example.rog.umk.Profile.userProfile;
import com.example.rog.umk.Statistic.statistic;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Fragment curSelected = null;
    BottomNavigationView bottomNavigationView;
    SharedPreferences prefs;
    FragmentManager fm;
    String id;
    boolean isLogin;
    String userType;
    public static Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        fm = getSupportFragmentManager();
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        isLogin = prefs.getBoolean("isLogin", false);
        userType = prefs.getString("type", "none");
        if (userType.equals("admin")){
            bottomNavigationView.getMenu().findItem(R.id.navigation_order).setTitle("Cases");
        }
        mainContext = this;
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                selectedFragment = Homepage.newInstance();
                                break;

                            case R.id.navigation_profile:
                                if (isLogin) {
                                    System.out.println("here");

                                    if (userType.equals("admin"))
                                        selectedFragment = adminProfile.newInstance();
                                    else if (userType.equals("seller") || userType.equals("koperasi") || userType.equals("buyer"))
                                        selectedFragment = userProfile.newInstance();
                                }
                                else
                                    selectedFragment = adminProfile.newInstance();
                                break;
                            case R.id.navigation_order:
                                selectedFragment = orderHistory.newInstance();
                                break;
                                /*
                            case R.id.navigation_stastic:
                                selectedFragment = statistic.newInstance();
                                break;*/

                        }
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Used to select an item programmatically
    }

    @Override
    protected void onResume() {
        super.onResume();
        MenuItem id = bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId());
        String selected = id.toString();
        System.out.println("selected:" + selected);
        if (selected.equals("Home"))
            curSelected = Homepage.newInstance();
        if (selected.equals("Profile")) {
            //curSelected = adminProfile.newInstance();
            if (isLogin) {
                System.out.println("here");
                String userType = prefs.getString("type", "");
                if (userType.equals("admin"))
                    curSelected = adminProfile.newInstance();
                else if (userType.equals("seller") || userType.equals("koperasi"))
                    curSelected = userProfile.newInstance();
            }
        }
        /*
        if (selected.equals("Me")) {
            if (!isLogin) {
                //View view = bottomNavigationView.findViewById(R.id.navigation_dashboard);
                //view.performClick();
                curSelected = category.newInstance();
            }
            else
                curSelected = profile.newInstance();
        }
        if (selected.equals("Discover"))
            curSelected = discover.newInstance();
            */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, curSelected);
        transaction.commit();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.out.println("mainactivity is destroyed");
    }
}
