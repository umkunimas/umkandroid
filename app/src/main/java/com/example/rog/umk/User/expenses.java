package com.example.rog.umk.User;

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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.Adapter.earningAdapter;
import com.example.rog.umk.Adapter.earningAdapterList;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class expenses extends Fragment {
    RecyclerView recyclerView;
    List<earningAdapterList> earningList;
    Context mCtx;
    String earn;
    TextView total;
    Double earn1 = 0.0;
    SharedPreferences prefs;
    String email = "";
    public expenses() {
        mCtx = getContext();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.user_profile_list, container, false);
        earningList = new ArrayList<>();
        total = rv.findViewById(R.id.total);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        recyclerView = rv.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadProducts();
        return rv;
    }

    private void loadProducts() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        email = prefs.getString("username", "none");
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/expenses.php?email=" + email;
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
                                earningList.add(new earningAdapterList(
                                        product.getString("date"),
                                        product.getString("activity"),
                                        earn = product.getString("expenses")
                                ));
                                earn1 = Double.parseDouble(earn) + earn1;

                            }
                            total.setText(earn1.toString());
                            //creating adapter object and setting it to recyclerview
                            earningAdapter adapter = new earningAdapter(getContext(), earningList);
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
}
