package com.example.owner.ecom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.ecom.prev.prevs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FinalOrder extends AppCompatActivity {

    private TextView txt, txt2;
    private EditText Fname, Fphone, Faddress, Fcity;
    private Button confirm;
    private String totPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order);


        txt = (TextView) findViewById(R.id.txt);
        txt2 = (TextView) findViewById(R.id.txt2);

        Fname = (EditText) findViewById(R.id.final_name);
        Fphone = (EditText) findViewById(R.id.final_phone);
        Faddress = (EditText) findViewById(R.id.final_adress);
        Fcity = (EditText) findViewById(R.id.final_city);

        confirm = (Button) findViewById(R.id.final_confirm);

        totPrice = getIntent().getStringExtra("total price");
        txt2.setText(txt2.getText() + " = $" + totPrice);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });




    }

    private void check() {

        if (TextUtils.isEmpty(Fname.getText().toString())){
            Toast.makeText(this, "please add your name !" , Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(Fphone.getText().toString())){
            Toast.makeText(this, "please add your phone !" , Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(Faddress.getText().toString())){
            Toast.makeText(this, "please add your Address !" , Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(Fcity.getText().toString())){
            Toast.makeText(this, "please add your city !" , Toast.LENGTH_LONG).show();
        } else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat Date = new SimpleDateFormat("MMM dd,yyyy");
        final String CurrentDate = Date.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        final String CurrentTime = time.format(calendar.getTime());

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(prevs.onlineUser.getPhone());
        HashMap <String, Object>  map = new HashMap<>();
        map.put("name", Fname.getText().toString());
        map.put("phone", Fphone.getText().toString());
        map.put("address", Faddress.getText().toString());
        map.put("city", Fcity.getText().toString());
        map.put("date", CurrentDate);
        map.put("time", CurrentTime);
        map.put("totalAmount", totPrice);
        map.put("state", "not shipped");

        reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(prevs.onlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(FinalOrder.this, "Your final order has confirmed" , Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(FinalOrder.this, Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });


    }
}
