package com.example.rog.umk.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rog.umk.R;

public class solve extends Fragment implements View.OnClickListener {
    Button viewSolve;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rv = inflater.inflate(R.layout.solve, container, false);
        viewSolve = rv.findViewById(R.id.viewSolve);
        viewSolve.setOnClickListener(this);
        System.out.println("here at solve");
        return rv;
    }

    @Override
    public void onClick(View v) {
        if (v == viewSolve){
            Intent intent = new Intent(getContext(), viewSolve.class);// This intent will be initiated
            startActivity(intent);
        }
    }
}
