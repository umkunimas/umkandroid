package com.example.rog.umk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rog.umk.Admin.viewSolveDetail;
import com.example.rog.umk.Admin.viewUnsolveDetail;
import com.example.rog.umk.R;

import java.util.List;

public class unsolveAdapter extends RecyclerView.Adapter<unsolveAdapter.HistoryViewHolder> {
    private Context mCtx;
    private List<SolveAdapterList> productList;

    public unsolveAdapter(Context mCtx, List<SolveAdapterList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    @NonNull
    public unsolveAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.solve_adapter_list, parent, false);
        return new unsolveAdapter.HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull unsolveAdapter.HistoryViewHolder holder, int position) {
        SolveAdapterList product = productList.get(position);

        holder.title.setText(product.getTitle());
        holder.date.setText(product.getDate());
        holder.id.setText(product.getId());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView date;
        TextView id;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            id = itemView.findViewById(R.id.id);
        }

        @Override
        public void onClick(View v) {
            System.out.println("onClick position: " + getAdapterPosition()+ "Id is: " + id.getText().toString());
            String pId = id.getText().toString();
            Intent intent = new Intent(mCtx, viewUnsolveDetail.class);
            intent.putExtra("id", pId);
            mCtx.startActivity(intent);
        }
    }
}
