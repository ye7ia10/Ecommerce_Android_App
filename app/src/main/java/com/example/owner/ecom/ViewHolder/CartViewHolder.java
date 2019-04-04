package com.example.owner.ecom.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.owner.ecom.Interfaces.ItemClickListner;
import com.example.owner.ecom.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtname, txtprice, txtquantity;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtname = itemView.findViewById(R.id.cart_name);
        txtprice = itemView.findViewById(R.id.cart_price);
        txtquantity = itemView.findViewById(R.id.cart_quantity);

    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
