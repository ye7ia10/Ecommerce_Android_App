package com.example.owner.ecom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.owner.ecom.Model.products;
import com.example.owner.ecom.ViewHolder.productViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class search extends AppCompatActivity {

    private EditText searchWord;
    private Button searchBtn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private String str = "";
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchWord = (EditText) findViewById(R.id.searchWord);
        searchBtn = (Button) findViewById(R.id.search_btn);

        recyclerView = (RecyclerView) findViewById(R.id.rec_search);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        reference = FirebaseDatabase.getInstance().getReference().child("Products");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = searchWord.getText().toString();
                if (TextUtils.isEmpty(str)){
                    Toast.makeText(search.this, "Type a word to search !!" , Toast.LENGTH_SHORT).show();
                }else {
                   onStart();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions <products> options =
                new FirebaseRecyclerOptions.Builder<products>()
                .setQuery(reference.orderByChild("name").equalTo(str.toLowerCase()), products.class)
                .build();

       FirebaseRecyclerAdapter<products, productViewHolder> adapter =
               new FirebaseRecyclerAdapter<products, productViewHolder>(options) {
                   @Override
                   protected void onBindViewHolder(@NonNull productViewHolder holder, int position, @NonNull final products model) {

                       holder.product_name.setText(model.getName());
                       holder.product_price.setText("Price = " + model.getPrice());
                       holder.product_desc.setText(model.getDescription());
                       Picasso.get().load(model.getImage()).into(holder.product_image);

                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               Intent intent = new Intent(search.this, productDetail.class);
                               intent.putExtra("pid", model.getPid());
                               startActivity(intent);
                           }
                       });
                   }

                   @NonNull
                   @Override
                   public productViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_layout, viewGroup, false);
                       productViewHolder productViewHolder = new productViewHolder(view);
                       return productViewHolder;
                   }
               };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
    }
}
