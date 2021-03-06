package com.example.rog.umk.Order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.Adapter.orderAdapter;
import com.example.rog.umk.Adapter.orderAdapterKoperasi;
import com.example.rog.umk.Adapter.orderAdapterKoperasiList;
import com.example.rog.umk.Adapter.orderAdapterList;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class previousOrder extends Fragment {
    RecyclerView recyclerView;
    List<orderAdapterKoperasiList> koperasiList;
    List<orderAdapterList> orderList;
    Context mCtx;
    String test1, test2;
    SharedPreferences prefs;
    String id;
    String usrType;
    String email;
    String URL_PRODUCTS;
    public previousOrder(){
        mCtx = getContext();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.order_history_list, container, false);
        orderList = new ArrayList<>();
        koperasiList = new ArrayList<>();
        recyclerView = rv.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        email = prefs.getString("username", "none");
        usrType = prefs.getString("type", "none");
        if (usrType.equals("koperasi"))
            loadProductsKoperasi();
        else
            loadProducts();
        return rv;
    }

    private void loadProductsKoperasi() {
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/orderList.php?st=1&tag=koperasi&email=" + email;
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
                                koperasiList.add(new orderAdapterKoperasiList(
                                        product.getString("name"),
                                        product.getString("img"),
                                        product.getString("amt"),
                                        product.getString("id"),
                                        product.getString("orderID"),
                                        product.getString("status")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            orderAdapterKoperasi adapter = new orderAdapterKoperasi(getContext(), koperasiList);
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

    private void loadProducts() {
        System.out.println("product");
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        if (usrType.equals("buyer"))
            URL_PRODUCTS = "https://umk-jkms.com/mobile/orderList.php?st=1&tag=buyer&email=" + email;
        if (usrType.equals("seller"))
            URL_PRODUCTS = "https://umk-jkms.com/mobile/orderList.php?st=1&tag=seller&email=" + email;
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
                            orderAdapter adapter = new orderAdapter(getContext(), orderList);
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
    @Override
    public void onPause() {
        super.onPause();
        System.out.println("here at onPause");

    }
}
