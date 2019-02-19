package com.example.rog.umk.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rog.umk.Adapter.SolveAdapterList;
import com.example.rog.umk.Adapter.newsAdapter;
import com.example.rog.umk.Adapter.newsAdapterList;
import com.example.rog.umk.Adapter.orderAdapter;
import com.example.rog.umk.Adapter.orderAdapter1;
import com.example.rog.umk.Adapter.orderAdapterList;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class profilePerniagaan extends AppCompatActivity {
    static String id;
    //a list to store all the products
    List<SolveAdapterList> productList;
    String URL_PRODUCTS;
    //the recyclerview
    RecyclerView recyclerView;
    List<orderAdapterList> orderList;
    SharedPreferences prefs;
    String sellerEmail;
    ImageView img;
    public boolean isLogin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isLogin = prefs.getBoolean("isLogin", false);
        if (isLogin) {
            setContentView(R.layout.seller_profile_perniagaan);
            Bundle bundle = getIntent().getExtras();
            sellerEmail = bundle.getString("sellerEmail");
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
            orderList = new ArrayList<>();
            //this method will fetch and parse json
            //to display it in recyclerview
            loadProducts();
        }

    }

    private void loadProducts() {
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        URL_PRODUCTS = "https://umk-jkms.com/mobile/orderList.php?st=1&tag=admin&email="+sellerEmail;
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
                                orderList.add(new orderAdapterList(
                                        product.getString("name"),
                                        product.getString("img"),
                                        product.getString("amt"),
                                        product.getString("price"),
                                        id = product.getString("id"),
                                        product.getString("date"),
                                        product.getString("orderID"),
                                        product.getString("email"),
                                        product.getString("type")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            orderAdapter1 adapter = new orderAdapter1(profilePerniagaan.this, orderList);
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
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}