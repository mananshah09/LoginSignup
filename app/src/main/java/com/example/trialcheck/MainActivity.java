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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //Variables
    FirebaseDatabase firebaseDatabase;
    EditText regName, regUsername, OTPText, regPhoneNo, regPassword;
    Button regBtn, regToLoginBtn,verify;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String name,username,otp,phoneNo,password;

    String mVerificationId;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth auth;
    String verificationId;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//Hooks to all xml elements in activity_sign_up.xml
        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        OTPText = findViewById(R.id.otptext);
        verify = findViewById(R.id.verify);
        verify.setEnabled(false);
        regPhoneNo = findViewById(R.id.reg_phoneNo);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_login_btn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        Log.d("Helloprachi", "onCreate: ");

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                verify.setEnabled(true);
                Log.d("TTTTTTAG", "onClick: ");
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                name = regName.getText().toString();
                username = regUsername.getText().toString();
                phoneNo = regPhoneNo.getText().toString();
                password = regPassword.getText().toString();
                final DatabaseReference personDetails = firebaseDatabase.getReference("users");
                personDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("going", "hello");
                        if (dataSnapshot.hasChild(phoneNo)) {
                            Log.d("trueee", dataSnapshot.child(phoneNo).getValue().toString());
                            Toast.makeText(MainActivity.this, "Already Exisitng", Toast.LENGTH_SHORT).show();
                        } else {



                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + phoneNo,        // Phone number to verify
                                    60,                 // Timeout duration
                                    TimeUnit.SECONDS,   // Unit of timeout
                                    MainActivity.this,// Activity (for callback binding)
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                            UserHelperClass helperClass = new UserHelperClass(name, username, phoneNo, password);
//                                            reference.child(phoneNo).setValue(helperClass);
//                                            Toast.makeText(MainActivity.this, "Data Submitted", Toast.LENGTH_SHORT).show();
//                                            Intent in = new Intent(MainActivity.this, Signin.class);
//                                            startActivity(in);
                                            signInWithPhoneAuthCredential(phoneAuthCredential);
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            Toast.makeText(MainActivity.this,"error  "+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                            Log.d("check1234","Hello");
                                            Log.d("check1234",s);
                                            verify.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String verificationCode = OTPText.getText().toString();//write this in otp button onclick function
                                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s, verificationCode);
                                                    signInWithPhoneAuthCredential(credential);
                                                }
                                            });
                                        }
                                    });



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }

                });
            }
        });

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Log.d("check1234", "Inside credential");

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("check1234", "signInWithCredential:success");
                            UserHelperClass helperClass = new UserHelperClass(name, username, phoneNo, password);
                                    reference.child(phoneNo).setValue(helperClass);
                                            Toast.makeText(MainActivity.this, "Data Submitted", Toast.LENGTH_SHORT).show();
                                            Intent in = new Intent(MainActivity.this, Signin.class);
                                            startActivity(in);
                            FirebaseUser user = task.getResult().getUser();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("check1234", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void datastore() {
        //just random data method not called in code
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                MainActivity.this,// Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        // register_user(phoneAuthCredential);//send data to database
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(MainActivity.this, "Verification Failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull final String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationID, forceResendingToken);
                        verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String verificationCode = OTPText.getText().toString();//write this in otp button onclick function
                                if (verificationCode.isEmpty()) {
                                    Toast.makeText(MainActivity.this, "Invalid Entry", Toast.LENGTH_SHORT).show();
                                } else if (verificationID.equalsIgnoreCase(verificationCode)) {
                                    Toast.makeText(MainActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                                    //pass details ki user is legit


                                } else {
                                    Toast.makeText(MainActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        auth.setLanguageCode("fr");
                    }
                });
    }






    public void goto_login (View view){
        Intent in = new Intent(MainActivity.this, Signin.class);
        startActivity(in);
    }
}