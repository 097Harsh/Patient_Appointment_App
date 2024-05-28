package com.example.patient_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class My_appoinment extends AppCompatActivity {

    private static final String TAG = "My_appoinment";
    private static final String SHARED_PREFS_NAME = "my_shared_prefs";
    private static final String SESSION_ID_KEY = "session_id";

    private String userId;

    private RecyclerView recyclerView;
    private ArrayList<Appointment> appointmentList;
    private DatabaseReference appointmentsRef;
    private MyUserAppoinmentAdapet adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_appoinment);

        userId = getIntent().getStringExtra("userId");

        // Log the user ID to console
        Log.d(TAG, "User ID: " + userId);

        // Initialize Firebase Database reference
        appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.my_appoinment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<>();
        adapter = new MyUserAppoinmentAdapet(this, appointmentList);
        recyclerView.setAdapter(adapter);

        // Fetch appointments for the current user
        fetchUserAppointments();
    }

    private void fetchUserAppointments() {
        // Query appointments for the current user from Firebase Database

        appointmentsRef.orderByChild("user_id").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appointmentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        appointmentList.add(appointment);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to fetch appointments: " + databaseError.getMessage());
            }
        });
    }
}
