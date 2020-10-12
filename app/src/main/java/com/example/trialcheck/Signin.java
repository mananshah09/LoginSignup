package com.example.trialcheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Signin extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    EditText pass, ph;
    String phoneno, password;
    String a, b;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        pass = findViewById(R.id.reg_password);

        ph = findViewById(R.id.reg_phoneNo);
        btn = findViewById(R.id.reg_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = pass.getText().toString();
                phoneno = ph.getText().toString();
                Log.d("password", password + "  " + phoneno);
                loadDataFromFireBase();


            }
        });


    }

    void loadDataFromFireBase() {

        //Whenever there is any update in assocaited data following code will be executed each time
        final DatabaseReference personDetails = firebaseDatabase.getReference("users");
        final DatabaseReference userdets = personDetails.child(phoneno);
        userdets.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    a = dataSnapshot.getKey();
                    b = dataSnapshot.child("password").getValue().toString();
                    Log.d("datasnap", b);
                    if(b.equalsIgnoreCase(password)){
                        Toast.makeText(Signin.this,"Logged in",Toast.LENGTH_SHORT).show();
                        Intent in=new Intent(Signin.this,User .class);
                        startActivity(in);
                    }
                    else{
                        Toast.makeText(Signin.this, "not there", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Signin.this, "not there", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}