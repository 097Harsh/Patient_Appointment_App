package com.example.patient_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Manage_Feed_back extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Feedback> list;
    DatabaseReference databaseReference;
    MyFAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_feed_back);

        //code fetching from firebase
        recyclerView = findViewById(R.id.recycleview1);
        databaseReference = FirebaseDatabase.getInstance().getReference("feedback");
        list = new ArrayList<Feedback>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyFAdapter(this,list);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Feedback feedback = dataSnapshot.getValue(Feedback.class);
                    list.add(feedback);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}