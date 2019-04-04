package com.example.owner.ecom.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.owner.ecom.Interfaces.ItemClickListner;
import com.example.owner.ecom.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView userName, orderPhone, orderAddress, orderPrice, orderDate;
    public Button shw;
    private ItemClickListner itemClickListner;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.order_user_name);
        orderPhone = itemView.findViewById(R.id.order_phone_number);
        orderAddress = itemView.findViewById(R.id.Order_address);
        orderDate = itemView.findViewById(R.id.order_date);
        orderPrice = itemView.findViewById(R.id.Order_price);
        shw = itemView.findViewById(R.id.showOrderDetail);

    }


    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
