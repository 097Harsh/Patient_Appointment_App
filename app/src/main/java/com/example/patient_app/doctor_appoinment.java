package com.example.patient_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

public class doctor_appoinment extends AppCompatActivity {
    private static final String TAG = "DoctorAppoinmentActivity";
    private static final String SHARED_PREFS_NAME = "my_shared_prefs";
    private static final String SESSION_ID_KEY = "session_id";
    private String userId;
    private String sessionId;
    private String userName;
    private RecyclerView recyclerView;
    private MyDoctorAppoinmentAdapter adapter;
    private ArrayList<Appointment> appointmentList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_appoinment);

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("username");
        Log.d(TAG, "User ID: " + userId);
        Log.d(TAG, "User Name: " + userName);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        sessionId = sharedPreferences.getString(SESSION_ID_KEY, "");
        Log.d(TAG, "Session ID: " + sessionId);

        recyclerView = findViewById(R.id.doctor_appoinment);
        appointmentList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("appointments");

        adapter = new MyDoctorAppoinmentAdapter(this, appointmentList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = databaseReference.orderByChild("doctorName").equalTo(userName);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear(); // Clear the list before adding new data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    Log.d(TAG, "Number of records retrieved: " + snapshot.getChildrenCount());

                    if (appointment != null) {
                        appointmentList.add(appointment);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event
                Log.e(TAG, "Error reading appointments: " + databaseError.getMessage());
            }
        });
    }
}