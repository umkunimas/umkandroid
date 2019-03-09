package com.example.rog.umk.Homepage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rog.umk.Adapter.ViewPageAdapter;
import com.example.rog.umk.Adapter.newsAdapter;
import com.example.rog.umk.Adapter.newsAdapterList;
import com.example.rog.umk.Login_Reg.login;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.Order.orderHistory;
import com.example.rog.umk.Product.Product;
import com.example.rog.umk.Product.ProductSearch;
import com.example.rog.umk.Product.addNewProduct;
import com.example.rog.umk.Product.cart;
import com.example.rog.umk.Product.productDetail;
import com.example.rog.umk.Profile.adminProfile;
import com.example.rog.umk.Profile.askForHelp;
import com.example.rog.umk.Profile.userProfile;
import com.example.rog.umk.R;
import com.example.rog.umk.app.Config;
import com.example.rog.umk.test;
import com.example.rog.umk.testScan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Homepage extends Fragment implements View.OnClickListener {
    ImageButton pasteri;
    ViewPager viewPager;
    ViewPageAdapter adapter;
    TextView t1,t2,t3,t4, st1, st2, st3, st4, ft1, ft2, ft3, ft4, bt1, bt2, bt3, bt4, seemore, seemore2, seemore3, seemore4, seemore5;
    ImageView new1,new2,new3,new4, s1, s2, s3, s4, f1, f2, f3, f4, b1, b2, b3, b4;
    String[] pname, serviceName, foodName, bulkName;
    String[] pimg, serviceImg, foodImg, bulkImg;
    String[] pId, serviceId, foodId, bulkId;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    RecyclerView recyclerView;
    List<newsAdapterList> productList;
    ProgressDialog progressDialog;
    String userType;
    View rv;
    private static final String ARG_KEY_NUMBER = "1";
    Context mCtx;
    private Class<?> mClss;
    SharedPreferences prefs;
    boolean isLogin= false;
    SharedPreferences sharedpreferences;
    private String[] images = {
            "https://umk-jkms.com/mobile/banner/banner1.jpg",
            "https://umk-jkms.com/mobile/banner/banner2.jpg",
            "https://umk-jkms.com/mobile/banner/banner3.jpg",
            "https://umk-jkms.com/mobile/banner/banner4.jpg",
            "https://umk-jkms.com/mobile/banner/banner5.jpg"
    };
    String tag;
    public static Homepage newInstance() {
        Homepage fragment = new Homepage();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.homepage, menu);
        MenuItem msearch = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) msearch.getActionView();
        String search = sv.getQuery().toString();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getContext(), ProductSearch.class);
                intent.putExtra("search", query);
                intent.putExtra("indi", "2");
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
        if (isLogin){
                MenuItem item = menu.findItem(R.id.login);
                item.setVisible(false);
        }
        else{
            MenuItem item = menu.findItem(R.id.login);
            item.setVisible(true);
            MenuItem item2 = menu.findItem(R.id.logout);
            item2.setVisible(false);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rv = inflater.inflate(R.layout.activity_homepage, container, false);
        viewPager = rv.findViewById(R.id.viewPager);
        adapter = new ViewPageAdapter(getActivity(),images);
        viewPager.setAdapter(adapter);
        seemore = rv.findViewById(R.id.seemore);
        seemore2 = rv.findViewById(R.id.seemore2);
        seemore3 = rv.findViewById(R.id.seemore3);
        seemore4 = rv.findViewById(R.id.seemore4);
        seemore5 = rv.findViewById(R.id.seemore5);
        seemore5.setOnClickListener(this);
        seemore.setOnClickListener(this);
        seemore2.setOnClickListener(this);
        seemore3.setOnClickListener(this);
        seemore4.setOnClickListener(this);

        Toolbar myToolbar = (Toolbar) rv.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(myToolbar);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isLogin = prefs.getBoolean("isLogin", false);
        System.out.println("islogin" + isLogin);
        userType = prefs.getString("username","none");
        mCtx = MainActivity.mainContext;
        t1 = rv.findViewById(R.id.tnew1);
        t2 = rv.findViewById(R.id.tnew2);
        t3 = rv.findViewById(R.id.tnew3);
        t4= rv.findViewById(R.id.tnew4);

        st1 = rv.findViewById(R.id.service1);
        st2 = rv.findViewById(R.id.service2);
        st3 = rv.findViewById(R.id.service3);
        st4 = rv.findViewById(R.id.service4);
        s1 = rv.findViewById(R.id.service1image);
        s2 = rv.findViewById(R.id.service2image);
        s3 = rv.findViewById(R.id.service3image);
        s4 = rv.findViewById(R.id.service4image);
        s1.setOnClickListener(this);
        s2.setOnClickListener(this);
        s3.setOnClickListener(this);
        s4.setOnClickListener(this);

        ft1 = rv.findViewById(R.id.food1);
        ft2 = rv.findViewById(R.id.food2);
        ft3 = rv.findViewById(R.id.food3);
        ft4 = rv.findViewById(R.id.food4);
        f1 = rv.findViewById(R.id.food1image);
        f2 = rv.findViewById(R.id.food2image);
        f3 = rv.findViewById(R.id.food3image);
        f4 = rv.findViewById(R.id.food4image);
        f1.setOnClickListener(this);
        f2.setOnClickListener(this);
        f3.setOnClickListener(this);
        f4.setOnClickListener(this);

        bt1 = rv.findViewById(R.id.bulk1);
        bt2 = rv.findViewById(R.id.bulk2);
        bt3 = rv.findViewById(R.id.bulk3);
        bt4 = rv.findViewById(R.id.bulk4);
        b1 = rv.findViewById(R.id.bulk1image);
        b2 = rv.findViewById(R.id.bulk2image);
        b3 = rv.findViewById(R.id.bulk3image);
        b4 = rv.findViewById(R.id.bulk4image);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);

        new1 = rv.findViewById(R.id.new1);
        new2 = rv.findViewById(R.id.new2);
        new3 = rv.findViewById(R.id.new3);
        new4 = rv.findViewById(R.id.new4);
        new1.setOnClickListener(this);
        new2.setOnClickListener(this);
        new3.setOnClickListener(this);
        new4.setOnClickListener(this);
        seemore.setOnClickListener(this);
        pname = new String[4];
        pimg = new String[4];
        pId = new String[4];

        serviceName = new String[4];
        serviceImg = new String[4];
        serviceId = new String[4];

        foodName = new String[4];
        foodImg = new String[4];
        foodId = new String[4];

        bulkName = new String[4];
        bulkImg = new String[4];
        bulkId = new String[4];

        recyclerView = rv.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
        productList = new ArrayList<>();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);



        /* For testing
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Fetching App List");
        progressDialog.setMessage("Please Wait...");
        */
        startProcess();

        return rv;
    }
    private void startProcess(){
        new LongOperation().execute();
    }
    private class LongOperation extends AsyncTask<String, Void, String> {
        ProgressDialog progDailog = new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            System.out.println("im her");
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            getWhatNew();
            getWhatNewService();
            getWhatNewFood();
            getWhatNewBulk();
            loadNews();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progDailog.dismiss();
        }
        //Used to select an item programmatically
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.scan:
                launchActivity(testScan.class);
                return true;
            case R.id.login:
                launchActivity(login.class);
                return true;
            case R.id.logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("isLogin");
                editor.remove("username");
                editor.remove("type");
                editor.remove("isLogin");
                editor.apply();
                getActivity().finish();
                launchActivity(MainActivity.class);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(getContext(), clss);
            startActivity(intent);
        }
    }
    private void loadNews() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/displayEventHomepage.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                productList.add(new newsAdapterList(
                                        product.getString("name"),
                                        product.getString("pImg"),
                                        product.getString("id"),
                                        product.getString("date")
                                ));

                            }

                            //creating adapter object and setting it to recyclerview
                            newsAdapter adapter = new newsAdapter(getContext(), productList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
    public void getWhatNew(){
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/displayProductHomepage.php?tag=Product";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                pname[i] = response1.getString("pname");
                                pimg[i] = response1.getString("thumb");
                                pId[i] = response1.getString("id");
                                System.out.println("Name: " + pname[i]);
                            }
                            setWhatNew();
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
    public void setWhatNew(){
        if (getActivity() != null) {
            Glide.with(this)
                    .load(pimg[0])
                    .transition(withCrossFade())
                    .into(new1);
            t1.setText(pname[0]);
            Glide.with(this)
                    .load(pimg[1])
                    .transition(withCrossFade())
                    .into(new2);

            t2.setText(pname[1]);
            Glide.with(this)
                    .load(pimg[2])
                    .transition(withCrossFade())
                    .into(new3);

            t3.setText(pname[2]);
            Glide.with(this)
                    .load(pimg[3])
                    .transition(withCrossFade())
                    .into(new4);

            t4.setText(pname[3]);
        }
        else
            return;
    }

    public void getWhatNewService() {
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/displayProductHomepage.php?tag=Service";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                serviceName[i] = response1.getString("pname");
                                serviceImg[i] = response1.getString("thumb");
                                serviceId[i] = response1.getString("id");
                                System.out.println("Name: " + pname[i]);
                            }
                            setService();
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
    public void setService(){
        if (getActivity() != null) {
            Glide.with(this)
                    .load(serviceImg[0])
                    .transition(withCrossFade())
                    .into(s1);
            st1.setText(serviceName[0]);
            Glide.with(this)
                    .load(serviceImg[1])
                    .transition(withCrossFade())
                    .into(s2);
            st2.setText(serviceName[1]);
            Glide.with(this)
                    .load(serviceImg[2])
                    .transition(withCrossFade())
                    .into(s3);
            st3.setText(serviceName[2]);
            Glide.with(this)
                    .load(serviceImg[3])
                    .transition(withCrossFade())
                    .into(s4);
            st4.setText(serviceName[3]);
        }
        else
            return;
    }


    public void getWhatNewFood() {
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/displayProductHomepage.php?tag=Food";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                foodName[i] = response1.getString("pname");
                                foodImg[i] = response1.getString("thumb");
                                foodId[i] = response1.getString("id");
                                System.out.println("Name: " + pname[i]);
                            }
                            setFood();
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
    public void setFood(){
        if (getActivity() != null) {
            Glide.with(this)
                    .load(foodImg[0])
                    .transition(withCrossFade())
                    .into(f1);
            ft1.setText(foodName[0]);
            Glide.with(this)
                    .load(foodImg[1])
                    .transition(withCrossFade())
                    .into(f2);
            ft2.setText(foodName[1]);
            Glide.with(this)
                    .load(foodImg[2])
                    .transition(withCrossFade())
                    .into(f3);
            ft3.setText(foodName[2]);
            Glide.with(this)
                    .load(foodImg[3])
                    .transition(withCrossFade())
                    .into(f4);
            ft4.setText(foodName[3]);
        }
        else
            return;
    }
    public void getWhatNewBulk() {
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/displayProductHomepage.php?tag=Bulk";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                bulkName[i] = response1.getString("pname");
                                bulkImg[i] = response1.getString("thumb");
                                bulkId[i] = response1.getString("id");
                                System.out.println("Name: " + pname[i]);
                            }
                            setBulk();
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
    public void setBulk(){
        if (getActivity() != null) {
            Glide.with(this)
                    .load(bulkImg[0])
                    .transition(withCrossFade())
                    .into(b1);
            bt1.setText(bulkName[0]);
            Glide.with(this)
                    .load(bulkImg[1])
                    .transition(withCrossFade())
                    .into(b2);
            bt2.setText(bulkName[1]);
            Glide.with(this)
                    .load(bulkImg[2])
                    .transition(withCrossFade())
                    .into(b3);
            bt3.setText(bulkName[2]);
            Glide.with(this)
                    .load(bulkImg[3])
                    .transition(withCrossFade())
                    .into(b4);
            bt4.setText(bulkName[3]);
        }
        else
            return;
    }

    @Override
    public void onClick(View v) {
            if (v == seemore) {
                tag = "Product";
                Intent intent = new Intent(getContext(), Product.class);
                intent.putExtra("tag", tag);
                intent.putExtra("indi","2");
                startActivity(intent);
            }
            if (v == seemore2) {
                tag = "Service";
                Intent intent = new Intent(getContext(), Product.class);
                intent.putExtra("tag", tag);
                intent.putExtra("indi","2");
                startActivity(intent);
            }
            if (v == seemore3) {
                tag = "Food";
                Intent intent = new Intent(getContext(), Product.class);
                intent.putExtra("tag", tag);
                intent.putExtra("indi","2");
                startActivity(intent);
            }

            if (v==seemore4) {
                tag = "Bulk";
                Intent intent = new Intent(getContext(), Product.class);
                intent.putExtra("tag", tag);
                intent.putExtra("indi","2");
                startActivity(intent);
            }
            if (v==seemore5){
                Intent intent = new Intent (getContext(), Product.class);
                intent.putExtra("tag", "event");
                intent.putExtra("indi","2");
                startActivity(intent);
            }
        if (v==f1){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", foodId[0]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==f2){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", foodId[1]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==f3){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", foodId[2]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==f4){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", foodId[3]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }

        if (v==s1){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", serviceId[0]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==s2){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", serviceId[1]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==s3){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", serviceId[2]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==s4){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", serviceId[3]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }

        if (v==b1){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", bulkId[0]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==b2){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", bulkId[1]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==b3){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", bulkId[2]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==b4){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", bulkId[3]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }

        if (v==new1){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", pId[0]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==new2){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", pId[1]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==new3){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", pId[2]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
        if (v==new4){
            Intent intent = new Intent(getContext(), productDetail.class);
            intent.putExtra("id", pId[3]);

                intent.putExtra("tag","order");
            startActivity(intent);
        }
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Running thread");
                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    }else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    } else if(viewPager.getCurrentItem() == 2) {
                        viewPager.setCurrentItem(3);
                    }else if(viewPager.getCurrentItem() == 3){
                        viewPager.setCurrentItem(4);
                    }else viewPager.setCurrentItem(0);
                }
            });
        }
    }

}
