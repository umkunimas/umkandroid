package com.example.rog.umk;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import com.example.rog.umk.Homepage.Homepage;
import com.example.rog.umk.Order.orderHistory;

import com.example.rog.umk.Profile.adminProfile;
import com.example.rog.umk.Profile.userProfile;

import com.example.rog.umk.app.Config;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Fragment curSelected = null;
    BottomNavigationView bottomNavigationView;
    SharedPreferences prefs;
    FragmentManager fm;
    String id;
    boolean isLogin;
    String userType;
    int position;
    String connection;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    public static Context mainContext;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().getExtras() != null)
        {
            connection = getIntent().getStringExtra("connection");
            if (connection.equals("true")) {
                mainContext = this;
                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        // checking for type intent filter
                        if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                            // gcm successfully registered
                            // now subscribe to `global` topic to receive app wide notifications
                            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                        } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                            // new push notification is received

                            String message = intent.getStringExtra("message");

                            Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                };
                bottomNavigationView = (BottomNavigationView)
                        findViewById(R.id.navigation);
                fm = getSupportFragmentManager();
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String regId = pref.getString("regId", null);

                System.out.println("Firebase reg id: " + regId);
                prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                isLogin = prefs.getBoolean("isLogin", false);
                userType = prefs.getString("type", "none");
                if (userType.equals("admin")) {
                    bottomNavigationView.getMenu().findItem(R.id.navigation_order).setTitle("Cases");
                }
                bottomNavigationView.setOnNavigationItemSelectedListener
                        (new BottomNavigationView.OnNavigationItemSelectedListener() {

                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                Fragment selectedFragment = Homepage.newInstance();
                                position = 0;
                                fm.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                switch (item.getItemId()) {
                                    case R.id.navigation_home:
                                        selectedFragment = Homepage.newInstance();
                                        break;
                                    case R.id.navigation_profile:
                                        Log.i("YEsssssssssssssssss","YEsssssssssssssssss");
                                        if (isLogin) {
                                            System.out.println("here");

                                            if (userType.equals("admin"))
                                                selectedFragment = adminProfile.newInstance(MainActivity.this);
                                            else if (userType.equals("seller") || userType.equals("koperasi") || userType.equals("buyer"))
                                                selectedFragment = userProfile.newInstance();
                                        } else
                                            selectedFragment = adminProfile.newInstance(MainActivity.this);

                                        break;
                                    case R.id.navigation_order:
                                        selectedFragment = orderHistory.newInstance(MainActivity.this);
                                        break;

                                }
                                FragmentTransaction transaction = fm.beginTransaction();
                                transaction.replace(R.id.frame_layout, selectedFragment);
                                transaction.commit();
                                return true;
                            }
                        });
                onFirstLoad();
            }
            else{
                showAlert();
            }
        }

    }

    public void goToHomeFragment()
    {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    public void showAlert(){
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

            alertDialog.setTitle("No Internet Connection");
            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            System.out.println("error mainactivity" + e.getMessage());
        }
    }
    public void onFirstLoad(){
        Fragment selectedFragment;
        position = 0;
        fm.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        selectedFragment = Homepage.newInstance();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
    }
    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("resumed mainActivity");
    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
