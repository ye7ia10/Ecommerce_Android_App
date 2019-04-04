package com.example.owner.ecom;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.owner.ecom.Model.Order;
import com.example.owner.ecom.ViewHolder.CartViewHolder;
import com.example.owner.ecom.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminOrders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        reference = FirebaseDatabase.getInstance().getReference().child("Orders");

        recyclerView = (RecyclerView) findViewById(R.id.recycle_Admin_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions
                .Builder<Order>()
                .setQuery(reference, Order.class)
                .build();

        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, final int position, @NonNull final Order model) {
                        holder.userName.setText("userName : " + model.getName().toString());
                        holder.orderPhone.setText("Phone : " + model.getPhone().toString());
                        holder.orderPrice.setText("Price : " + model.getTotalAmount().toString());
                        holder.orderAddress.setText("Address : " + model.getAddress().toString() + " , " + model.getCity());
                        holder.orderDate.setText("Date : " + model.getTime().toString() + " , " + model.getDate());

                        holder.shw.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uid = getRef(position).getKey();
                                Intent intent = new Intent(AdminOrders.this, orderDetail.class);
                                intent.putExtra("phone", uid);
                                startActivity(intent);
                            }
                        });


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence [] options = new CharSequence[] {"Yes", "No"};

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminOrders.this);
                                alertDialog.setTitle("Have you shipped this order ?");
                                alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0){
                                            String uid = getRef(position).getKey();
                                            removeOrder(uid);
                                        }else if (i == 1){
                                            finish();
                                        }
                                    }
                                });
                                alertDialog.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.neworderadmin, viewGroup, false);
                        OrderViewHolder cartViewHolder =  new OrderViewHolder(view);
                        return cartViewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public void removeOrder(final String id){
        reference.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()){
                    final DatabaseReference reference1 =FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("Admin View")
                            .child(id);
                    reference1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AdminOrders.this, "Order Shipped Successfully", Toast
                                        .LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
    }
}
