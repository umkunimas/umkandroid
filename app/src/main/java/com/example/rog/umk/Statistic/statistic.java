package com.example.rog.umk.Statistic;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rog.umk.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class statistic extends Fragment {

    String mon, tue, wed, thu, fri, sat, sun;
    String earning1, earning2, earning3, earning4, earning5, earning6, earning7;
    Double e1, e2, e3, e4, e5, e6, e7;
    Date d1,d2,d3,d4;
    GraphView graph;
    Context mCtx;
    SimpleDateFormat dateParser;
    public static statistic newInstance(){
        statistic fragment = new statistic();

        return fragment;
    }
    public statistic(){
        mCtx = getContext();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.statistic, container, false);
        graph = rv.findViewById(R.id.graph);
        dateParser = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        getValue();
        setGraph();
        return rv;
    }

    private void setGraph(){
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Date");
        gridLabel.setVerticalAxisTitle("Earning");
        graph.setTitle("October");
        try {
            d1 = dateParser.parse(mon);
            d2 = dateParser.parse(tue);
            d3 = dateParser.parse(wed);
            d4 = dateParser.parse(thu);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, e1),
                new DataPoint(d2, e2),
                new DataPoint(d3, e3),
                new DataPoint(d4, e4)
        });

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(mCtx));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(4);
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.addSeries(series);

    }

    public void getValue() {
        mon = "01-10-2018";
        tue = "08-10-2018";
        wed = "15-10-2018";
        thu = "22-10-2018";
        e1 = 10.00;
        e2 = 15.00;
        e3 = 20.00;
        e4 = 30.00;
    }
}
