package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText lEmail, lPassword;
    TextView lForgotPassword, English,French;
    FirebaseFirestore lStore;
    FirebaseAuth lAuth,mAuth;
    Button lLogin,lRegister, lAdminLogin;
    Boolean valid = true;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadlocale();
        setContentView(R.layout.activity_main);

        lEmail = findViewById(R.id.editTxt_Email);
        lPassword = findViewById(R.id.editTxt_Password);
        lLogin = findViewById(R.id.btn_login);
        lRegister = findViewById(R.id.btn_register);
        lForgotPassword = findViewById(R.id.txt_ForgotPassword);
        English = findViewById(R.id.txt_english);
        French = findViewById(R.id.txt_french);
        lStore = FirebaseFirestore.getInstance();
        lAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate("en");
                recreate();
            }
        });

        French.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate("fr");
                recreate();

            }
        });

        /**
         * take back to register page
         */

        lRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                finish();;
            }
        });

        /**
         * performing login activity
         */

        if(valid){


            lLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkField(lEmail);
                    checkField(lPassword);
                    lAuth.signInWithEmailAndPassword(lEmail.getText().toString(),lPassword.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    DocumentReference docRef = db.collection("Users").document(user.getUid());
                                    docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (value.exists()) {
                                                //update
                                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                                checkAccessLevel(authResult.getUser().getUid());
                                            } else {
                                                //Insert
                                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                user.delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(MainActivity.this, "fail login", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG","Error Login "+ e.getMessage());
                            Toast.makeText(MainActivity.this, "fail login", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });

        }



    }

    /**
     * Checking access levels for users
     * @param uid String
     */

    private void checkAccessLevel(String uid) {
        DocumentReference documentReference = lStore.collection("Users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","onSuccess" + documentSnapshot.getData());
                if (documentSnapshot.getString("isUser").equals("0")){
                    startActivity(new Intent(getApplicationContext(),AdminHomePage.class));
                    finish();
                }
                if (documentSnapshot.getString("isUser").equals("1")){
                    startActivity(new Intent(getApplicationContext(),TenantHomePage.class));
                    finish();
                }
                if(documentSnapshot.getString("isUser").equals("2")){
                    Intent intent = new Intent(getApplicationContext(),RenterHomePage.class);
                    startActivity(intent);
                }
            }
        });
    }


    /**
     * Checking text field is not empty
     * @param TextField
     * @return Boolean
     */

    private Boolean checkField(EditText TextField) {

        if(TextField.getText().toString().isEmpty()){
            TextField.setError("Error");
            valid = false;
        }else{
            valid = true;
        }
        return valid;

    }

    /**
     * Checking user previously logged-n or not
     */
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").
                    document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot.getString("isUser").equals("0")){
                        startActivity(new Intent(getApplicationContext(),AdminHomePage.class));
                        finish();
                    }
                    if (documentSnapshot.getString("isUser").equals("1")){
                        startActivity(new Intent(getApplicationContext(),TenantHomePage.class));
                        finish();
                    }
                    if(documentSnapshot.getString("isUser").equals("2")){
                        Intent intent = new Intent(getApplicationContext(),RenterHomePage.class);
                        startActivity(intent);
                    }


                }
            });

        }
    }
    private void translate(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();

    }

    private void loadlocale(){

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        translate(language);
    }
}