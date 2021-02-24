package com.example.rentalapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.rentalapp.tenantfragments.MyadsFragment;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FirstFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    String userID;
    List<SlideModel> slideModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        recyclerView = v.findViewById(R.id.recycler_view4);
        firebaseFirestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = firebaseFirestore.collection("Myads").document(userID).collection("Selected");

        FirestoreRecyclerOptions<Model> options = new FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query,Model.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Model, ItemViewHolder>(options) {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.renter_myad_recycler_view,parent,false);
                return new ItemViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Model model) {


                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("ApartmentImages").
                        document(model.getDocumentid());
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int count = Integer.valueOf(documentSnapshot.getString("count"));
                        for (int i=0; i<count;i++){
                            slideModels.add(new SlideModel(documentSnapshot.getString("image"+i)));
                        }

                        holder.Image.setImageList(slideModels,true);
                    }
                });

                holder.Name.setText(model.getStreetname());
                holder.Price.setText(model.getPrice());
                holder.Description.setText(model.getDescription());
                holder.Place.setText(model.getPlace());
                String docid = model.getDocumentid();

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ApartmentEditDetails.class);
                        intent.putExtra("Id",model.getDocumentid());
                        intent.putExtra("Price",model.getPrice());
                        intent.putExtra("Title",model.getStreetname());
                        intent.putExtra("Place",model.getPlace());
                        intent.putExtra("Description",model.getDescription());
                        startActivity(intent);
                    }
                });

                holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.edit){
                            Intent intent = new Intent(getContext(), ApartmentEditDetails.class);
                            intent.putExtra("Id",model.getDocumentid());
                            intent.putExtra("Price",model.getPrice());
                            intent.putExtra("Title",model.getStreetname());
                            intent.putExtra("Place",model.getPlace());
                            intent.putExtra("Description",model.getDescription());
                            startActivity(intent);
                        }
                        if (id == R.id.delete){

                            DocumentReference eRef = db.collection("Myads").document(userID).collection("Selected").document(docid);
                            eRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "removed", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            DocumentReference bRef = db.collection("Apartments").document(docid);
                            bRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "removed", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                        }
                        return false;
                    }
                });


            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(adapter);

        return v;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView Price;
        TextView Name;
        TextView Description;
        TextView Place;
        ImageSlider Image;
        String id;
        Toolbar toolbar;
        CardView cardView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Price = itemView.findViewById(R.id.txt_pricemyads);
            Image = itemView.findViewById(R.id.Img_renter_apartment);
            Name = itemView.findViewById(R.id.txt_titlemyads);
            Place = itemView.findViewById(R.id.txt_placemyads);
            Description = itemView.findViewById(R.id.txt_descriptionmyads);
            toolbar = itemView.findViewById(R.id.toolbar_myads);
            cardView = itemView.findViewById(R.id.cardview_renter_myad);
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