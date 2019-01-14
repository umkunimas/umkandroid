package com.example.rog.umk.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.Adapter.SolveAdapter;
import com.example.rog.umk.Adapter.SolveAdapterList;
import com.example.rog.umk.Login_Reg.login;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class viewSolve extends AppCompatActivity {

    static String id;
    String email;
    //a list to store all the products
    List<SolveAdapterList> productList;
    String URL_PRODUCTS;
    //the recyclerview
    RecyclerView recyclerView;
    SharedPreferences prefs;
    public boolean isLogin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isLogin = prefs.getBoolean("isLogin", false);
        if (isLogin) {
            setContentView(R.layout.activity_view_solve_list);
            //getting the recyclerview from xml
            recyclerView = findViewById(R.id.recylcerView);
            id = prefs.getString("username", "none");
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(viewSolve.this));
            recyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
            String userType = prefs.getString("type", "");
            email = prefs. getString("username","none");
            if (userType.equals("seller"))
                URL_PRODUCTS = "https://umk-jkms.com/mobile/viewHelp.php?tag=1&email=" + email;
            if (userType.equals("admin"))
                URL_PRODUCTS = "https://umk-jkms.com/mobile/viewHelp.php?tag=1&email=admin";
            //initializing the productlist
            productList = new ArrayList<>();
            System.out.println("At onCreate like");
            //this method will fetch and parse json
            //to display it in recyclerview
            loadProducts();
        }
        else{
            Intent intent = new Intent(viewSolve.this, login.class);// This intent will be initiated
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
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
                                productList.add(new SolveAdapterList(
                                        product.getString("title"),
                                        product.getString("date"),
                                        product.getInt("id")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            SolveAdapter adapter = new SolveAdapter(viewSolve.this, productList);
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
}
