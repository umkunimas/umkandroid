package com.example.rog.umk.Order;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rog.umk.Profile.askForHelp;
import com.example.rog.umk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class customerOrderDetail extends AppCompatActivity {
    ImageView img;
    TextView name, buyerName, date, total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_item);

        img = findViewById(R.id.img);
        name = findViewById(R.id.name);
        buyerName = findViewById(R.id.buyer);
        date = findViewById(R.id.date);
        total = findViewById(R.id.total);

    }
}
