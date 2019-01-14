package com.example.rog.umk.User;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;



import com.example.rog.umk.Adapter.SectionPageAdapter;
import com.example.rog.umk.Admin.solve;
import com.example.rog.umk.Admin.unsolve;

import com.example.rog.umk.R;

import java.util.Objects;


public class userHelp extends AppCompatActivity {
    private ViewPager viewPager;
    private SectionPageAdapter mSectionPageAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_help);
        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        ((AppCompatActivity) Objects.requireNonNull(this)).setSupportActionBar(myToolbar);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager vp){
        System.out.println("here");
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new viewSolveUser(), "Solve");
        adapter.addFragment(new unsolve(), "Unsolved");
        vp.setAdapter(adapter);
    }
}
