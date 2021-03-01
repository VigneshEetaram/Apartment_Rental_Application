package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminRenterChtatroom extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth fAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_renter_chtatroom);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewadminrenterchatroom);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        String renterid = intent.getExtras().getString("renterid");
        Query query = firebaseFirestore.collection("Chatroom").whereEqualTo("renterid",renterid);
        FirestoreRecyclerOptions<ChatUserModel> options = new FirestoreRecyclerOptions.Builder<ChatUserModel>()
                .setQuery(query,ChatUserModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ChatUserModel, ItemViewHolder>(options) {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleradminchatroom,parent,false);
                return new ItemViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull ChatUserModel model) {

                holder.Chatroomname.setText(model.getChatroomname());
                holder.renter.setText(model.getRenterid());
                holder.tenant.setText(model.getTenantid());


                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), AdminRenterChatRoomActivity.class);
                        intent.putExtra("tenantid",model.getTenantid());
                        intent.putExtra("documentid",model.getDocumentid());
                        intent.putExtra("renterid",model.getRenterid());
                        intent.putExtra("chatid",model.getChatid());
                        intent.putExtra("chatroomname",model.getChatroomname());
                        startActivity(intent);
                    }
                });

            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(adapter);

    }
    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView Chatroomname;
        TextView renter;
        TextView tenant;
        CardView cardView;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_layout_chat_admin_chatroom2);
            renter = itemView.findViewById(R.id.txt_chatlist_admin_rentername);
            tenant = itemView.findViewById(R.id.txt_chatlist_admin_tenantname);
            Chatroomname = itemView.findViewById(R.id.txt_chatlist_admin_renterchatroom);
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