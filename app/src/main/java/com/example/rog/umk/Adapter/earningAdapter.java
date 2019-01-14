package com.example.rog.umk.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rog.umk.R;

import java.util.List;

public class earningAdapter extends RecyclerView.Adapter<earningAdapter.HistoryViewHolder> {
private Context mCtx;
private List<earningAdapterList> productList;

public earningAdapter(Context mCtx, List<earningAdapterList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        }

@Override
@NonNull
public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.user_profile_adapter, parent,false);
        return new HistoryViewHolder(view);
        }


@Override
public void onBindViewHolder(@NonNull  HistoryViewHolder holder, int position) {
        earningAdapterList product = productList.get(position);
        //loading the image

        holder.activity.setText(product.getActivity());
        holder.date.setText(product.getDate());
        holder.price.setText(product.getPrice());
        //holder.id.setText(product.getId());
        }

@Override
public int getItemCount() {
        return productList.size();
        }

class HistoryViewHolder extends RecyclerView.ViewHolder{
    TextView activity, price;
    TextView date;
    public HistoryViewHolder(View itemView) {
        super(itemView);
        activity = itemView.findViewById(R.id.activity);
        price = itemView.findViewById(R.id.price);
        date = itemView.findViewById(R.id.date);
    }

        /*@Override
        public void onClick(View v) {
            System.out.println("onClick position: " + getAdapterPosition()+ "Id is: " + id.getText().toString());
            String pId = id.getText().toString();
            Intent intent = new Intent(mCtx, product.class);
            intent.putExtra("id", pId);
            mCtx.startActivity(intent);
        }*/
}
}
