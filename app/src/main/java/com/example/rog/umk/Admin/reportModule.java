package com.example.rog.umk.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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

import com.example.rog.umk.Adapter.ProductAdapter;
import com.example.rog.umk.Product.Product;
import com.example.rog.umk.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class reportModule extends AppCompatActivity implements View.OnClickListener{

    TextView tot, filter;
    String total;
    BarChart barChart;
    BarChart barChart2;
    String spnNumber;
    Spinner spn;
    Button search;
    String search1, division;
    String URL_PRODUCTS;
    TextView div;
    CardView blank1, blank2;
    double earningProduct, earningFood, earningService;
    int numberProduct, numberFood, numberService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_module);
        tot = findViewById(R.id.umk);
        barChart = (BarChart) findViewById(R.id.barchart);
        barChart2 = (BarChart) findViewById(R.id.barchart2);
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        div = findViewById(R.id.division);
        filter = findViewById(R.id.filter);
        Bundle bundle = getIntent().getExtras();
        blank1 = findViewById(R.id.blank);
        blank2 = findViewById(R.id.blank2);
        spn  = (Spinner)findViewById(R.id.spn);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.complaint_arrays1, R.layout.myspinner);
        adapter.setDropDownViewResource(R.layout.myspinnerdrop);
        spn.setAdapter(adapter);
        search1 = spn.getSelectedItem().toString();
        div.setText(search1);
        URL_PRODUCTS = "http://umk-jkms.com/mobile/reportModule.php?tag=" + search1;
        getDate();
    }
    private void getDate() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        div.setText(search1);
        URL_PRODUCTS = "http://umk-jkms.com/mobile/reportModule.php?tag=" + search1;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                total = response1.getString("count");
                                earningProduct = response1.getDouble("product");
                                earningFood = response1.getDouble("food");
                                earningService = response1.getDouble("service");
                                numberFood = response1.getInt("numberFood");
                                numberService = response1.getInt("numberService");
                                numberProduct = response1.getInt("numberProduct");
                                if (total.equals("0")) {
                                    tot.setText("No Data");
                                    blank1.setVisibility(GONE);
                                    blank2.setVisibility(GONE);
                                }
                                else
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

    public void loadFromSite(){
        tot.setText(total);



        ArrayList<BarEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
        xEntry.add("Product");
        xEntry.add("Food");
        xEntry.add("Service");

        yEntry.add(new BarEntry((float)earningProduct,0));
        yEntry.add(new BarEntry((float)earningFood,1));
        yEntry.add(new BarEntry((float)earningService,2));
        BarDataSet bardataset = new BarDataSet(yEntry, "");
        BarData data = new BarData(xEntry, bardataset);
        barChart.setData(data);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelsToSkip(0);
        barChart.setDescription("Field Earning");
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(5000);

        //for other barchart
        ArrayList<BarEntry> yEntry2 = new ArrayList<>();
        ArrayList<String> xEntry2 = new ArrayList<>();
        xEntry2.add("Product");
        xEntry2.add("Food");
        xEntry2.add("Service");

        yEntry2.add(new BarEntry((float)numberProduct,0));
        yEntry2.add(new BarEntry((float)numberFood,1));
        yEntry2.add(new BarEntry((float)numberService,2));
        BarDataSet bardataset2 = new BarDataSet(yEntry2, "");
        BarData data2 = new BarData(xEntry2, bardataset2);
        barChart2.setData(data2);
        XAxis xAxis2 = barChart2.getXAxis();
        xAxis2.setLabelsToSkip(0);
        barChart2.setDescription("Item by Division");
        bardataset2.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart2.animateY(5000);
        /*
        PieDataSet pieDataSet = new PieDataSet(yEntry, "Division Earning");
        pieDataSet.setSliceSpace(1);
        pieDataSet.setValueTextSize(12);



        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        */

    }

    @Override
    public void onClick(View v) {
        if (v==search){
            search1 = spn.getSelectedItem().toString();
            System.out.println("string1" + search1);
            URL_PRODUCTS = "http://umk-jkms.com/mobile/reportModule.php?tag=" + search1;
            getDate();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
