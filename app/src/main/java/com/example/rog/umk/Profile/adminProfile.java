package com.example.rog.umk.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rog.umk.Adapter.SectionPageAdapter;
import com.example.rog.umk.Admin.ListOfSeller;
import com.example.rog.umk.Admin.adminAction;
import com.example.rog.umk.Admin.event;
import com.example.rog.umk.Admin.reportModule;
import com.example.rog.umk.Admin.solve;
import com.example.rog.umk.Admin.unsolve;
import com.example.rog.umk.Login_Reg.login;
import com.example.rog.umk.Login_Reg.registerSeller;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.Product.addNewProduct;
import com.example.rog.umk.R;
import com.example.rog.umk.User.userHelp;
import com.example.rog.umk.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class adminProfile extends Fragment {
    private ViewPager viewPager;
    private SectionPageAdapter mSectionPageAdapter;
    ImageView imgV;
    private Toolbar toolbar;
    private TextView name, phone;
    static String email;
    String name1, img1;
    SharedPreferences prefs;
    String userType;
    public boolean isLogin;
    public static adminProfile newInstance(){
        adminProfile fragment = new adminProfile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.admin, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isLogin = prefs.getBoolean("isLogin", false);
        userType = prefs.getString("type","none");
        View rv = null;
        if (isLogin) {
            rv = inflater.inflate(R.layout.activity_admin_profile, container, false);
            Toolbar myToolbar = (Toolbar) rv.findViewById(R.id.my_toolbar);
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(myToolbar);
            name = rv.findViewById(R.id.name);
            phone = rv.findViewById(R.id.phone);
            mSectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());
            email = prefs. getString("username","none");
            imgV = rv.findViewById(R.id.imageView);
            viewPager = rv.findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            TabLayout tabLayout = rv.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            System.out.println("profile");
            loadUser();
        }
        else{
            Intent intent = new Intent(getActivity(), login.class);// This intent will be initiated
            startActivity(intent);
        }
        return rv;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                getActivity().finish();
                Toast.makeText(getContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();
                intent = new Intent(getContext(), MainActivity.class);// This intent will be initiated
                startActivity(intent);
                return true;
            case R.id.register:
                intent = new Intent (getContext(), registerSeller.class);
                startActivity(intent);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    public void loadUser(){
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/loadUser.php?id=" + email;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                name.setText(response1.getString("name"));
                                phone.setText(response1.getString("tel"));
                                img1 = response1.getString("img");
                                setUser();
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
    public void setUser(){
        if (getActivity() != null) {
            Glide.with(this)
                    .load(img1)
                    .transition(withCrossFade())
                    .into(imgV);
        }
        else
            return;
    }
    private void setupViewPager(ViewPager vp){
        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager());
        adapter.addFragment(new adminAction(), "Action");

        vp.setAdapter(adapter);
    }
}
