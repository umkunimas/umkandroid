package com.example.rog.umk.Admin;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class reportModule extends AppCompatActivity {

    TextView tot;
    String total;
    BarChart barChart;
    PieChart pieChart;
    double kuching, samarahan, serian, sriaman, bintulu, miri, mukah, kapit, limbang, sarikei, betong,sibu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_module);
        tot = findViewById(R.id.umk);
        barChart = (BarChart) findViewById(R.id.barchart);
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
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/reportModule.php";
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
                                kapit = response1.getDouble("kapit");
                                kuching = response1.getDouble("kuching");
                                bintulu = response1.getDouble("bintulu");
                                sibu = response1.getDouble("sibu");
                                miri = response1.getDouble("miri");
                                mukah = response1.getDouble("mukah");
                                limbang = response1.getDouble("limbang");
                                sarikei = response1.getDouble("sarikei");
                                sriaman = response1.getDouble("sriaman");
                                samarahan = response1.getDouble("samarahan");
                                betong = response1.getDouble("betong");
                                serian = response1.getDouble("serian");
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
        xEntry.add("KCH");
        xEntry.add("SBU");
        xEntry.add("SMH");
        xEntry.add("SRI");
        xEntry.add("SRK");
        xEntry.add("KPT");
        xEntry.add("LMB");
        xEntry.add("Miri");
        xEntry.add("BTG");
        xEntry.add("BTU");
        xEntry.add("SRN");
        xEntry.add("MKH");

        yEntry.add(new BarEntry((float)kuching,0));
        yEntry.add(new BarEntry((float)sibu,1));
        yEntry.add(new BarEntry((float)samarahan,2));
        yEntry.add(new BarEntry((float)sriaman,3));
        yEntry.add(new BarEntry((float)sarikei,4));
        yEntry.add(new BarEntry((float)kapit,5));
        yEntry.add(new BarEntry((float)limbang,6));
        yEntry.add(new BarEntry((float)miri,7));
        yEntry.add(new BarEntry((float)betong,8));
        yEntry.add(new BarEntry((float)bintulu,9));
        yEntry.add(new BarEntry((float)serian,10));
        yEntry.add(new BarEntry((float)mukah,11));
        BarDataSet bardataset = new BarDataSet(yEntry, "");
        BarData data = new BarData(xEntry, bardataset);
        barChart.setData(data);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelsToSkip(0);
        barChart.setDescription("Division Earning");
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(5000);
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

}
