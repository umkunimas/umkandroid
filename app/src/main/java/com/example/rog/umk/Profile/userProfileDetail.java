package com.example.rog.umk.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rog.umk.Adapter.SectionPageAdapter;
import com.example.rog.umk.Admin.event;
import com.example.rog.umk.Admin.solve;
import com.example.rog.umk.Admin.unsolve;
import com.example.rog.umk.Login_Reg.login;
import com.example.rog.umk.R;
import com.example.rog.umk.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class userProfileDetail extends AppCompatActivity {
    private ViewPager viewPager;
    private SectionPageAdapter mSectionPageAdapter;
    ImageView imgV;
    private Toolbar toolbar;
    private TextView name, phone;
    static String email;
    String name1, img1;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String sellerEmail;
    public boolean isLogin;
    SharedPreferences sharedpreferences;
    @Override
    public void onCreate(Bundle savedInstanceStatee) {
        super.onCreate(savedInstanceStatee);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedpreferences.edit();
        isLogin = prefs.getBoolean("isLogin", false);
        View rv = null;
        if (isLogin) {
            setContentView(R.layout.seller_profile_setup);
            Toolbar myToolbar = (Toolbar) rv.findViewById(R.id.my_toolbar);
            ((AppCompatActivity) Objects.requireNonNull(this)).setSupportActionBar(myToolbar);
            mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

            TabLayout tabLayout = rv.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            System.out.println("profile");
            Bundle bundle = getIntent().getExtras();
            sellerEmail = bundle.getString("sellerEmail");
            editor.putString("sellerEmail", sellerEmail);
            editor.commit();
        } else {
            Intent intent = new Intent(this, login.class);// This intent will be initiated
            startActivity(intent);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("userProfileDetail is destroyed");
        editor.remove("sellerEmail");
        editor.commit();
    }
}
