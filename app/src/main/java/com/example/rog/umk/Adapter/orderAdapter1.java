package com.example.rog.umk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rog.umk.Product.productDetail;
import com.example.rog.umk.R;
import com.example.rog.umk.qrCode;

import java.util.List;

import static android.view.View.GONE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class orderAdapter1 extends RecyclerView.Adapter<orderAdapter1.HistoryViewHolder> {
    private Context mCtx;
    private List<orderAdapterList> productList;
    int qty;
    String total;
    String orderID;
    public orderAdapter1(Context mCtx, List<orderAdapterList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    @NonNull
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.order_history_adapter1, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  HistoryViewHolder holder, int position) {
        orderAdapterList product = productList.get(position);
        //loading the image
        Glide.with(mCtx)
                .load(product.getImg())
                .transition(withCrossFade())
                .into(holder.imageView);
        holder.price.setText(product.getPrice());
        holder.name.setText(product.getName());
        holder.id.setText(product.getId());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date, price;
        ImageView imageView;
        TextView amt;
        TextView name, rm, qty;
        CardView card;
        TextView id;
        TextView order;
        TextView buyer;
        SharedPreferences prefs;
        String email, usrType;
        ImageButton qr;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            id = itemView.findViewById(R.id.id);
            price = itemView.findViewById(R.id.price);
            name = itemView.findViewById(R.id.name);
            rm = itemView.findViewById(R.id.rm);
            card = itemView.findViewById(R.id.card);
            card.setOnClickListener(this);
            prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
            usrType = prefs.getString("type", "none");
            email = prefs. getString("username","none");
            System.out.println("usertype" + usrType);

        }

        public void onClick(View v) {
            /*System.out.println("onClick position: " + getAdapterPosition()+ "Id is: " + id.getText().toString());
            String pId = id.getText().toString();
            Intent intent = new Intent(mCtx, product.class);
            intent.putExtra("id", pId);
            mCtx.startActivity(intent);*/

            if (v == card){
                Intent intent = new Intent(mCtx, productDetail.class);
                intent.putExtra("id", id.getText().toString());
                intent.putExtra("tag", "display");
                mCtx.startActivity(intent);
            }
        }
    }
}
