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

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.HistoryViewHolder> {
        private Context mCtx;
        private List<cartAdapterList> productList;

        public cartAdapter(Context mCtx, List<cartAdapterList> productList) {
            this.mCtx = mCtx;
            this.productList = productList;
        }

        @Override
        @NonNull
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.cart_adapter, null);
            return new HistoryViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
            cartAdapterList product = productList.get(position);
            //loading the image
            Glide.with(mCtx)
                    .load(product.getImg())
                    .transition(withCrossFade())
                    .into(holder.imageView);

            holder.amt.setText(product.getAmt());
            holder.price.setText(product.getPrice());
            holder.name.setText(product.getName());
            holder.id.setText(product.getId());
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        class HistoryViewHolder extends RecyclerView.ViewHolder {
            TextView date, price;
            ImageView imageView;
            TextView amt;
            TextView name;
            TextView id;

            public HistoryViewHolder(View itemView) {
                super(itemView);
                id = itemView.findViewById(R.id.id);
                imageView = itemView.findViewById(R.id.imageView);
                amt = itemView.findViewById(R.id.amt);
                price = itemView.findViewById(R.id.itemPrice);
                name = itemView.findViewById(R.id.itemName);
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
