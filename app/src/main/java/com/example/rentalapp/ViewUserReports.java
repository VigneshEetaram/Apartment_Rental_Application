package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewUserReports extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth fAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_reports);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewviewuserreports);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("Userreports");
        FirestoreRecyclerOptions<ViewReportsModel> options = new FirestoreRecyclerOptions.Builder<ViewReportsModel>()
                .setQuery(query,ViewReportsModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<ViewReportsModel, ItemViewHolder>(options) {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewuserreports,parent,false);
                return new ItemViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull ViewReportsModel model) {


                holder.date.setText("Date: " +model.getDate());
                holder.name.setText("UersID: " +model.getUserid());
                holder.title.setText("Title: " +model.getTitle());
                holder.descritpion.setText("Description: " +model.getDescription());
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(adapter);

    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView name;
        TextView title;
        TextView descritpion;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_view_user_reports_date);
            name = itemView.findViewById(R.id.txt_view_user_reports_username);
            title = itemView.findViewById(R.id.txt_view_user_reports_title);
            descritpion = itemView.findViewById(R.id.txt_view_user_reports_description);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
