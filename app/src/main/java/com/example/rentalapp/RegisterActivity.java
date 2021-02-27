package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText mName, mEmail, mPassword, mPhoneNumber;
    ImageView mImage;
    Button mRegisterBTN, mLoginBTN;
    Boolean valid = true;
    RadioButton mTenant, mRenter;
    int uservalue;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = (EditText) findViewById(R.id.register_name);
        mEmail = (EditText)findViewById(R.id.register_Email);
        mPassword = (EditText)findViewById(R.id.register_password);
        mPhoneNumber = (EditText)findViewById(R.id.register_phone);
        mRegisterBTN = (Button) findViewById(R.id.register_BTN);
        mRenter = (RadioButton) findViewById(R.id.radio_Register_Renter);
        mTenant = (RadioButton)findViewById(R.id.radio_Register_Tenant);
        mLoginBTN = (Button)findViewById(R.id.login_BTN);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        /**
         * if tenant box is checked,this method check other one
         */

        mTenant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    mRenter.setChecked(false);
                }
            }
        });

        /**
         * if renter box is checked,this method check other one
         */

        mRenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    mTenant.setChecked(false);
                }
            }
        });

        mLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        mRegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }



    private void registerUser() {
        final String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        final String name = mName.getText().toString().trim();
        final String number = mPhoneNumber.getText().toString().trim();

        if(name.isEmpty()){
            mName.setError("Fullname is required");
            mName.requestFocus();
            return;
        }
        if(number.isEmpty()){

            mPhoneNumber.setError("Number is required");
            mPhoneNumber.requestFocus();
            return;
        }
        if(email.isEmpty()){

            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Provide valid email");
            mEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){

            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return;
        }
        if(password.length() < 6 ){
            mPassword.setError("Minimum length should be 6 characters");
            mPassword.requestFocus();
            return;
        }
        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = fAuth.getCurrentUser();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Toast.makeText(RegisterActivity.this, "Register Successfull", Toast.LENGTH_SHORT).show();
                    DocumentReference documentReference = fStore.collection("Users").document(user.getUid());
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("FullName", name);
                    userInfo.put("UserID", userID);
                    userInfo.put("Email", email);
                    userInfo.put("PhoneNumber", number);

                    Map<String, Object> users = new HashMap<>();
                    users.put("FullName", name);
                    users.put("UserID", userID);
                    users.put("Email", email);
                    users.put("PhoneNumber", number);

                    if(mTenant.isChecked()){
                        userInfo.put("isUser", "1");
                        uservalue = 1;
                        DocumentReference documentReferences = fStore.collection("Tenant").document(user.getUid());
                        documentReferences.set(userInfo);
                    }

                    if(mRenter.isChecked()){
                        userInfo.put("isUser", "2");
                        uservalue = 2;
                        DocumentReference documentReferences = fStore.collection("Renter").document(user.getUid());
                        documentReferences.set(userInfo);
                    }
                    documentReference.set(userInfo);

                    if(uservalue == 1){
                        startActivity(new Intent(getApplicationContext(), TenantHomePage.class));
                        finish();
                    }

                    if(uservalue == 2){
                        startActivity(new Intent(getApplicationContext(), RenterHomePage.class));
                        finish();
                    }

                }
                else{
                    Toast.makeText(RegisterActivity.this, "Failed to register! Try again.", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Failed to register! Try again.", Toast.LENGTH_SHORT).show();

            }
        });



    }
}
