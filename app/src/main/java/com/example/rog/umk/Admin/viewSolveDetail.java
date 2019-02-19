package com.example.rog.umk.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class viewSolveDetail extends AppCompatActivity {

    TextView titleTv, dateTv, idTv, nameTv, statusTv, descTv;
    String id, title, date, name, status, desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_solve_detail);
        titleTv = findViewById(R.id.title);
        descTv = findViewById(R.id.desc);
        dateTv = findViewById(R.id.date);
        idTv = findViewById(R.id.id);
        nameTv = findViewById(R.id.name);
        statusTv = findViewById(R.id.status);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        loadCase();
    }

    private void loadCase() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/viewHelpDetail.php?id=" + id;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                name = response1.getString("name");
                                status = response1.getString("status");
                                title = response1.getString("title");
                                date = response1.getString("date");
                                desc = response1.getString("desc");
                                loadFromSite();
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void loadFromSite(){
        titleTv.setText(title);
        dateTv.setText(date);
        descTv.setText(desc);
        statusTv.setText(status);
        nameTv.setText(name);
        idTv.setText(id);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
