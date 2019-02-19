package com.example.rog.umk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class viewEvent extends AppCompatActivity {

    TextView titleTv, dateTv, idTv, locationTv, statusTv, descTv;
    String id, title, date, name, status, desc, imgS, location;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        titleTv = findViewById(R.id.name);
        descTv = findViewById(R.id.description);
        dateTv = findViewById(R.id.date);
        locationTv = findViewById(R.id.location);
        img = findViewById(R.id.img);
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
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/getEventDetail.php?id=" + id;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                title = response1.getString("title");
                                desc = response1.getString("desc");
                                date = response1.getString("date");
                                location = response1.getString("location");
                                imgS = response1.getString("img");
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
       locationTv.setText(location);
        Glide.with(this)
                .load(imgS)
                .transition(withCrossFade())
                .into(img);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

