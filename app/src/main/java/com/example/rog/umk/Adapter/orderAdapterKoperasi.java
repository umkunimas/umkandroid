package com.example.rog.umk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rog.umk.Order.koperasiList;
import com.example.rog.umk.R;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class orderAdapterKoperasi extends RecyclerView.Adapter<orderAdapterKoperasi.HistoryViewHolder> {
    private Context mCtx;
    private List<orderAdapterKoperasiList> productList;

    public orderAdapterKoperasi(Context mCtx, List<orderAdapterKoperasiList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    @NonNull
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.order_history_koperasi_adapter, null);
        return new HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull  HistoryViewHolder holder, int position) {
        orderAdapterKoperasiList product = productList.get(position);
        //loading the image
        Glide.with(mCtx)
                .load(product.getImg())
                .transition(withCrossFade())
                .into(holder.imageView);

        holder.name.setText(product.getName());
        holder.id.setText(product.getID());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView amt;
        TextView name;
        TextView id;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.id);
        }

        @Override
        public void onClick(View v) {
            System.out.println("onClick position: " + getAdapterPosition()+ "Id is: " + id.getText().toString());
            String pId = id.getText().toString();
            String name1 = name.getText().toString();
            Intent intent = new Intent(mCtx, koperasiList.class);
            intent.putExtra("id", pId);
            intent.putExtra("name", name1);
            mCtx.startActivity(intent);
        }
    }
}
