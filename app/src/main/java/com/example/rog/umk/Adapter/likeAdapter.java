package com.example.rog.umk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rog.umk.Product.productDetail;
import com.example.rog.umk.R;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class likeAdapter extends RecyclerView.Adapter<likeAdapter.HistoryViewHolder> {
    private Context mCtx;
    private List<likeAdapterList> productList;
    String id;
    String img1;
    public likeAdapter(Context mCtx, List<likeAdapterList> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    @NonNull
    public likeAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.like_adapter, parent,false);
        return new likeAdapter.HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull likeAdapter.HistoryViewHolder holder, int position) {
        likeAdapterList product = productList.get(position);
        //loading the image

        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice());
        holder.id.setText(product.getId());
        img1 = product.getImg();
        Glide.with(mCtx)
                .load(img1)
                .transition(withCrossFade())
                .into(holder.img);
        //holder.id.setText(product.getId());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, price;
        ImageView img;
        CardView card;
        TextView id;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            card.setOnClickListener(this);
            id = itemView.findViewById(R.id.itemID);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.desc);
            img = itemView.findViewById(R.id.img);
        }

        @Override
        public void onClick(View v) {
            String pId = id.getText().toString();
            Intent intent = new Intent (mCtx, productDetail.class);
            intent.putExtra("id", pId);
            intent.putExtra("tag", "display");
            mCtx.startActivity(intent);
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
