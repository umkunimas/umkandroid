package com.example.rog.umk.Admin;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.Adapter.koperasiAdapter;
import com.example.rog.umk.Order.koperasiList;
import com.example.rog.umk.R;
import com.example.rog.umk.koperasiAdapterList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class ListOfSeller extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    List<koperasiAdapterList> productList;
    String id;
    String name;
    SharedPreferences prefs;
    String pName, search1;
    Button search;
    Spinner spn;
    Toolbar mActionBarToolbar;
    String userType;
    TextView blank;
    String URL_PRODUCTS;
    TextView filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koperasi_list);
        mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("List of Seller");
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        spn  = (Spinner)findViewById(R.id.spn);
        blank = findViewById(R.id.blank);
        filter = findViewById(R.id.filter);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.complaint_arrays1, R.layout.myspinner);
        adapter.setDropDownViewResource(R.layout.myspinnerdrop);
        spn.setAdapter(adapter);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userType = prefs.getString("type","none");
        if (userType.equals("admin")) {
            URL_PRODUCTS = "https://umk-jkms.com/mobile/getSellerList.php?tag=Kuching";
        }
        else {
            URL_PRODUCTS = "https://umk-jkms.com/mobile/koperasiList.php?tag=list&name=" + pName + "&search=Kuching";
            spn.setVisibility(GONE);
            filter.setVisibility(GONE);
            search.setVisibility(GONE);
        }
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
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            if (array.length() > 0) {
                                blank.setVisibility(GONE);
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);

                                    //adding the product to product list
                                    productList.add(new koperasiAdapterList(
                                            name = product.getString("name"),
                                            product.getString("img"),
                                            product.getString("tel"),
                                            product.getString("email"),
                                            product.getString("id")
                                    ));
                                    System.out.println("email" + product.getString("email"));
                                }

                            }
                            else{
                                blank.setVisibility(View.VISIBLE);
                                blank.setText("No seller found in this division");
                            }
                            //creating adapter object and setting it to recyclerview
                            koperasiAdapter adapter = new koperasiAdapter(ListOfSeller.this, productList, "list", "none");
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
    public void onClick(View v) {
        if (v==search){
            search1 = spn.getSelectedItem().toString();
            if (userType.equals("admin"))
                URL_PRODUCTS = "https://umk-jkms.com/mobile/getSellerList.php?tag="+search1;
            else
                URL_PRODUCTS = "https://umk-jkms.com/mobile/koperasiList.php?tag=list&name=" + pName + "&search=" + search1;
            loadNews();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
