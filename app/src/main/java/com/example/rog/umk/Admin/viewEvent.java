package com.example.rog.umk.Admin;

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
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class viewEvent extends AppCompatActivity {
    ImageView img;
    TextView name, location, description,date;
    String name1, location1, description1, date1, img1;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        name = findViewById(R.id.name);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
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
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/eventDetail.php?id=" + id;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                img1 = response1.getString("img");
                                name1 = response1.getString("title");
                                location1 = response1.getString("location");
                                description1 = response1.getString("desc");
                                date1 = response1.getString("date");
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
        name.setText(name1);
        date.setText(date1);
        description.setText(description1);
        location.setText(location1);
        Glide.with(this)
                .load(img1)
                .transition(withCrossFade())
                .into(img);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
