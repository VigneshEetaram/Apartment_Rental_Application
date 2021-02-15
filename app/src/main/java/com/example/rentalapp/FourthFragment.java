package com.example.rentalapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FourthFragment extends Fragment {

    EditText firstName, lastName, email, phoneNumber;
    TextView changePassword, LogOut;
    private FirebaseFirestore firebaseFirestore;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    String userID;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fourth, container, false);
        firstName = v.findViewById(R.id.editTxt_Renter_FN);
        lastName = v.findViewById(R.id.editTxt_Renter_LN);
        email = v.findViewById(R.id.editTxt_Renter_email);
        phoneNumber = v.findViewById(R.id.editTxt_Renter_phone);
        changePassword = v.findViewById(R.id.txtView_Renter_Change_Password);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),UpdatePassword.class));
            }
        });

        DocumentReference docRef = db.collection("Users").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                firstName.setText(user.getFullname());
                email.setText(user.getEmails());
                phoneNumber.setText(user.getPhonenumber());
            }
        });




        LogOut = v.findViewById(R.id.txtView_Renter_Logout);
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(),MainActivity.class));
            }
        });
        return  v;
    }
}