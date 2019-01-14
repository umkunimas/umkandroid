package com.example.rog.umk;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rog.umk.Product.addOrder;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class testScan extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    String text, total, email, id, orderID;
    com.android.volley.RequestQueue requestQueue;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_test_scan);
        setupToolbar();
        requestQueue = Volley.newRequestQueue(testScan.this);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        text = rawResult.getText();
        String[] arrOfStr = text.split("&", 4);
        String[] info = new String[4];
        int i = 0;
        for (String a : arrOfStr){
            info[i] = a;
            System.out.println(info[i]);
            i++;
        }
        total = info[2];
        id = info[1];
        orderID = info[3];
        email = info[0];

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(testScan.this);
            }
        }, 2000);
        postInfo();
    }
    public void postInfo(){
        System.out.println("total" + total);
        System.out.println("id" + id);
        System.out.println("order" + orderID);
        System.out.println("email" + email);
        String UPLOAD_URL;
        UPLOAD_URL = "https://umk-jkms.com/mobile/pay.php";

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        if (ServerResponse.equals("success")){
                            Toast.makeText(testScan.this, "Successfully paid", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent (testScan.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(testScan.this, "No internet connection", Toast.LENGTH_LONG).show();
                            System.out.println("server" + ServerResponse);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(testScan.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("total", total);
                params.put("id", id);
                params.put("orderID", orderID);
                params.put("email", email);
                return params;
            }
        };
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(testScan.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }
}

