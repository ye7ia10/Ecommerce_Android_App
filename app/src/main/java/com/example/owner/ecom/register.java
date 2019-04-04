package com.example.owner.ecom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class register extends AppCompatActivity {

    private Button makeAcc;
    private EditText name, phone, pass;
    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        makeAcc = (Button) findViewById(R.id.reg);
        name = (EditText) findViewById(R.id.name_reg);
        phone = (EditText) findViewById(R.id.phoneNumber_reg);
        pass = (EditText) findViewById(R.id.password_reg);
        progressBar = new ProgressDialog(this);
        makeAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAcc();
            }
        });
    }

    public void createAcc(){
        String user = name.getText().toString();
        String phoneNom = phone.getText().toString();
        String password = pass.getText().toString();

        if (TextUtils.isEmpty(user)){
            Toast.makeText(this, "Enter your name ... ", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(phoneNom)){
            Toast.makeText(this, "Enter your phone number ... ", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter your password ... ", Toast.LENGTH_LONG).show();
        }else {
            progressBar.setTitle("Making the account");
            progressBar.setMessage("please wait while making the account");
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.show();

            validate(user, phoneNom, password);
        }
    }

    public void validate(final String user, final String phoneNum, final String Password){
        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /**
                 * check if the number already exist
                 * making hash map with new values if correct
                 * pass the hash map to the fire base
                 */
                if (! (dataSnapshot.child("Users").child(phoneNum).exists())){
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("phone", phoneNum);
                    userData.put("password", Password);
                    userData.put("name", user);

                    rootref.child("Users").child(phoneNum).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(register.this, "Your account is made .. ", Toast.LENGTH_LONG).show();
                                progressBar.dismiss();
                                Intent intent = new Intent(register.this, Login.class);
                                startActivity(intent);
                            } else {
                                progressBar.dismiss();
                                Toast.makeText(register.this, "Error has occurred ...", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                } else {
                    Toast.makeText(register.this, "this phone number is already exists .. ", Toast.LENGTH_LONG).show();
                    progressBar.dismiss();
                    Toast.makeText(register.this, "Try again using another number .. ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(register.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
