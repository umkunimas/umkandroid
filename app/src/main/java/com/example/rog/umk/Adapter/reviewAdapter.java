package com.example.rog.umk.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rog.umk.R;

import java.util.List;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.HistoryViewHolder> {
    private Context mCtx;
    private List<reviewAdapterList> productList;

    public reviewAdapter(Context mCtx, List<reviewAdapterList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    @NonNull
    public reviewAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_review_adapter, null);
        return new reviewAdapter.HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull reviewAdapter.HistoryViewHolder holder, int position) {
        reviewAdapterList product = productList.get(position);

        holder.name.setText(product.getName());
        holder.date.setText(product.getDate());
        holder.review.setText(product.getReview());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView date;
        TextView review;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            review = itemView.findViewById(R.id.review);
        }

    }
}
