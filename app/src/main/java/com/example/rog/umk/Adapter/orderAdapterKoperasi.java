package com.example.rog.umk.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rog.umk.Order.koperasiList;
import com.example.rog.umk.Order.koperasiMyOrder;
import com.example.rog.umk.R;
import com.example.rog.umk.qrCode;

import java.util.List;

import static android.view.View.GONE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class orderAdapterKoperasi extends RecyclerView.Adapter<orderAdapterKoperasi.HistoryViewHolder> {
    private Context mCtx;
    private List<orderAdapterKoperasiList> productList;
    String email;
    SharedPreferences prefs;
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
        holder.order.setText(product.getOrderId());
        holder.amt.setText(product.getAmt());
        holder.status.setText(product.getStatus());
        if (product.getStatus().equals("1")) {
            holder.complete.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView amt;
        TextView name;
        TextView id, order;
        ImageButton complete;
        Button myorder;
        CardView card;
        TextView status;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
            email = prefs.getString("username", "none");
            imageView = itemView.findViewById(R.id.imageView);
            order = itemView.findViewById(R.id.order);
            status = itemView.findViewById(R.id.status);
            name = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.id);
            complete = itemView.findViewById(R.id.complete2);
            amt = itemView.findViewById(R.id.price);
            complete.setOnClickListener(this);
            myorder = itemView.findViewById(R.id.myorder);
            myorder.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v==itemView) {
                if (status.getText().toString().equals("2")) {
                    System.out.println("onClick position: " + getAdapterPosition() + "Id is: " + id.getText().toString());
                    String orderID = order.getText().toString();
                    String name1 = name.getText().toString();
                    Intent intent = new Intent(mCtx, koperasiList.class);
                    intent.putExtra("id", orderID);
                    intent.putExtra("name", name1);
                    mCtx.startActivity(intent);
                }
                else{
                    //do nothing
                }
            }
            if (v == complete){
                String total = amt.getText().toString();
                String str=email + "&" + id.getText().toString() + "&" + total + "&" + order.getText().toString(); // Whatever you need to encode in the QR code
                Intent intent = new Intent(mCtx, qrCode.class);
                intent.putExtra("info", str);
                mCtx.startActivity(intent);
            }
            if (v==myorder){
                String orderID = order.getText().toString();
                Intent intent = new Intent(mCtx, koperasiMyOrder.class);
                intent.putExtra("orderID", orderID);
                mCtx.startActivity(intent);
            }
        }
    }
}
