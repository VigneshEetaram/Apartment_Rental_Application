package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore lStore;
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView title, slogan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        image=findViewById(R.id.splashlogo);
        title=findViewById(R.id.title);

        image.setAnimation(topAnim);
        title.setAnimation(bottomAnim);

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);


                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };myThread.start();
    }
}