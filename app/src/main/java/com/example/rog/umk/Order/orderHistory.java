package com.example.rog.umk.Order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rog.umk.Adapter.SectionPageAdapter;
import com.example.rog.umk.Admin.solve;
import com.example.rog.umk.Admin.unsolve;
import com.example.rog.umk.Login_Reg.login;
import com.example.rog.umk.R;

public class orderHistory extends Fragment {
    private ViewPager viewPager;
    private SectionPageAdapter mSectionPageAdapter;
    private static final String ARG_KEY_NUMBER = "3";
    private Toolbar toolbar;
    private TextView name, phone;
    static String email;
    String name1, img1;
    SharedPreferences prefs;
    public boolean isLogin;
    public static orderHistory newInstance(){

        orderHistory fragment = new orderHistory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isLogin = prefs.getBoolean("isLogin", false);
        View rv = null;
        if (isLogin) {
            rv = inflater.inflate(R.layout.order_history, container, false);

            mSectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());

            viewPager = rv.findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            TabLayout tabLayout = rv.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            System.out.println("profile");

        }
        else{
            Intent intent = new Intent(getActivity(), login.class);// This intent will be initiated
            startActivity(intent);
        }
        return rv;
    }

    private void setupViewPager(ViewPager vp){
        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager());
        String userType = prefs.getString("type","none");
        if (userType.equals("seller") || userType.equals("buyer")) {
            adapter.addFragment(new currentOrder(), "Current Order");
            adapter.addFragment(new previousOrder(), "Previous Order");
        }
        else if (userType.equals("koperasi")){
            adapter.addFragment(new currentOrder(), "Current Order");
            adapter.addFragment(new previousOrder(), "Previous Order");
        }
        else
        {
            adapter.addFragment(new solve(), "Solve");
            adapter.addFragment(new unsolve(), "Unsolved");
        }
        vp.setAdapter(adapter);
    }
}