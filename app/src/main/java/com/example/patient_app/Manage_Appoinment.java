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

public class Manage_Appoinment extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Appointment> list;
    DatabaseReference databaseReference;
    MyAppoinmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_appoinment);

        // Initialize RecyclerView and other variables
        recyclerView = findViewById(R.id.recycleview2); // Make sure you have the correct ID
        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("appointments");

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAppoinmentAdapter(this, list);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list before adding new data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    list.add(appointment);
                }
                adapter.notifyDataSetChanged(); // Notify adapter of data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }
}
