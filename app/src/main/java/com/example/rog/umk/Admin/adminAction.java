package com.example.rog.umk.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rog.umk.R;

public class adminAction extends Fragment implements View.OnClickListener {
    Button viewProfile, event, report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.admin_action, container, false);
        viewProfile = rv.findViewById(R.id.userProfile);
        event = rv.findViewById(R.id.event);
        report = rv.findViewById(R.id.report);
        event.setOnClickListener(this);
        report.setOnClickListener(this);
        viewProfile.setOnClickListener(this);
        return rv;
    }

    @Override
    public void onClick(View v) {
        if (v == viewProfile) {
            Intent intent = new Intent(getContext(), ListOfSeller.class);// This intent will be initiated
            startActivity(intent);
        }
        if (v == event) {
            Intent intent = new Intent(getContext(), event.class);// This intent will be initiated
            startActivity(intent);
        }
        if (v == report) {
            Intent intent = new Intent(getContext(), reportModule.class);// This intent will be initiated
            startActivity(intent);
        }
    }
}
