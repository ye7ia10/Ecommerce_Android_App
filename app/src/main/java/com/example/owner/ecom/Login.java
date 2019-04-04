package com.example.owner.ecom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.owner.ecom.Model.Users;
import com.example.owner.ecom.prev.prevs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText inNom, inPass;
    private ProgressDialog progressBar;
    private CheckBox checkBoxrm;
    private TextView admin, notadmin;
    private String DataName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login = (Button) findViewById(R.id.login);
        inNom = (EditText) findViewById(R.id.phoneNumber_input);
        inPass = (EditText) findViewById(R.id.password_input);
        checkBoxrm = (CheckBox) findViewById(R.id.remember_chbx);
        admin = (TextView) findViewById(R.id.admin_link);
        notadmin = (TextView) findViewById(R.id.not_admin_link);


        Paper.init(this);
        progressBar = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTo();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setText("Login Admin");
                admin.setVisibility(View.INVISIBLE);
                notadmin.setVisibility(View.VISIBLE);
                DataName= "Admins";
            }
        });

        notadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setText("Login");
                admin.setVisibility(View.VISIBLE);
                notadmin.setVisibility(View.INVISIBLE);
                DataName = "Users";
            }
        });
    }

    public void loginTo(){

        String phoneNom = inNom.getText().toString();
        String password = inPass.getText().toString();

        if (TextUtils.isEmpty(phoneNom)){
            Toast.makeText(this, "Enter your phone number ... ", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter your password ... ", Toast.LENGTH_LONG).show();
        }else {
            progressBar.setTitle("logging into account..");
            progressBar.setMessage("please wait while logging the account");
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.show();

            allow(phoneNom, password);
        }
    }


    public  void  allow (final String phone, final String pass){

        //check box to save the current data for Remember Me
        if (checkBoxrm.isChecked()){
            Paper.book().write(prevs.userPhoneKey, phone);
            Paper.book().write(prevs.userPassKey, pass);
        }

        //data base fire base variables
        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();
        //check if the number exist
        //get data using the number from fire base
        //the phone number is the unique key for the data base to retrieve it
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(DataName).child(phone).exists()){

                    Users userData = dataSnapshot.child(DataName).child(phone).getValue(Users.class);
                    Log.e("phone", userData.getName());
                   if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(pass)){
                          if (DataName.equals("Users")){
                              Toast.makeText(Login.this, "You are logged ... ", Toast.LENGTH_LONG).show();
                              progressBar.dismiss();
                              prevs.onlineUser = userData;
                              Intent intent = new Intent(Login.this, Home.class);
                              startActivity(intent);
                              finish();
                          } else if (DataName.equals("Admins")){
                              Toast.makeText(Login.this, "You are logged ... ", Toast.LENGTH_LONG).show();
                              progressBar.dismiss();
                              Intent intent = new Intent(Login.this, AdminCat.class);
                              startActivity(intent);
                              finish();
                          }
                        } else {
                            Toast.makeText(Login.this, "wrong password ... ", Toast.LENGTH_LONG).show();
                            progressBar.dismiss();
                        }
                  }

                }else {
                    progressBar.dismiss();
                    Toast.makeText(Login.this, "This number does not exist ... ", Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
