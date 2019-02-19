package com.example.rog.umk.Order;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.Adapter.SampleRecycler;

import com.example.rog.umk.Adapter.koperasiMyOrderAdapter;
import com.example.rog.umk.Adapter.koperasiMyOrderAdapterList;
import com.example.rog.umk.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class koperasiMyOrder extends AppCompatActivity{
    RecyclerView recyclerView;
    List<koperasiMyOrderAdapterList> productList;

    String id;
    SharedPreferences prefs;
    String name;
    String pName;
    String email;
    String orderID, search1;
    Bundle bundle;
    Spinner spn;
    Button search;
    TextView blank;
    LinearLayout srch;
    Toolbar mActionBarToolbar;
    String URL_PRODUCTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koperasi_list);
        mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("My Order");
        srch = findViewById(R.id.srch);
        srch.setVisibility(GONE);
        blank = findViewById(R.id.blank);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
        productList = new ArrayList<>();
        recyclerView.setAdapter(new SampleRecycler());
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        email = prefs.getString("username", "none");
        bundle = getIntent().getExtras();
        orderID = bundle.getString("orderID");
        System.out.println("orderID" + orderID);
        URL_PRODUCTS = "https://umk-jkms.com/mobile/koperasiList.php?tag=order&name=" + email +"&orderID=" + orderID;
        loadNews();
    }
    private void loadNews() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        productList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            blank.setVisibility(GONE);
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);

                                    //adding the product to product list
                                    productList.add(new koperasiMyOrderAdapterList(
                                            product.getString("name"),
                                            product.getString("img"),
                                            product.getString("tel"),
                                            product.getString("sellerName"),
                                            product.getString("orderID"),
                                            product.getString("date"),
                                            product.getString("total"),
                                            product.getString("qty"),
                                            product.getString("sellerEmail"),
                                            product.getString("status")
                                    ));
                                }
                            }
                            else {
                                blank.setVisibility(View.VISIBLE);
                                blank.setText("You haven't make any order");
                            }
                            koperasiMyOrderAdapter adapter = new koperasiMyOrderAdapter(koperasiMyOrder.this, productList);
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
        Volley.newRequestQueue(koperasiMyOrder.this).add(stringRequest);
    }

}
