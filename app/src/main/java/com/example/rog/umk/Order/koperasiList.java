package com.example.rog.umk.Order;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.Adapter.SampleRecycler;
import com.example.rog.umk.Adapter.koperasiAdapter;

import com.example.rog.umk.R;
import com.example.rog.umk.koperasiAdapterList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;

public class koperasiList extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    List<koperasiAdapterList> productList;

    String orderID;
    String name;
    Button search;
    Spinner spn;
    String pName, search1;
    TextView blank;
    String tag = "list";
    String URL_PRODUCTS;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koperasi_list);
        recyclerView = findViewById(R.id.recyclerView);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("List of Seller");
        setSupportActionBar(myToolbar);
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        spn  = (Spinner)findViewById(R.id.spn);
        blank = findViewById(R.id.blank);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.complaint_arrays1, R.layout.myspinner);
        adapter.setDropDownViewResource(R.layout.myspinnerdrop);
        spn.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
        productList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        recyclerView.setAdapter(new SampleRecycler());
        orderID = bundle.getString("id");
        System.out.println("orderID at koperasiList: " + orderID);
        pName = bundle.getString("name");
        URL_PRODUCTS = "https://umk-jkms.com/mobile/koperasiList.php?tag=list&name=" + pName + "&search=Kuching";
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
                                            product.getString("sellerEmail"),
                                            product.getString("id")
                                    ));
                                    System.out.println("name is: " + name);
                                }
                            }
                            else{
                                blank.setVisibility(View.VISIBLE);
                                blank.setText("No seller found in this division");
                            }
                            koperasiAdapter adapter = new koperasiAdapter(koperasiList.this, productList, tag, orderID);
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
            URL_PRODUCTS = "https://umk-jkms.com/mobile/koperasiList.php?tag=list&name=" + pName + "&search=" + search1;
            loadNews();
        }
    }
}
